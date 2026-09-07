package com.klef.sdp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klef.sdp.dto.AvailabilityResponse;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.dto.HoldRequest;
import com.klef.sdp.dto.InventoryRequest;
import com.klef.sdp.dto.InventoryResponse;
import com.klef.sdp.entity.Booking;
import com.klef.sdp.entity.BookingStatus;
import com.klef.sdp.entity.EventInventory;
import com.klef.sdp.entity.HoldStatus;
import com.klef.sdp.entity.SeatHold;
import com.klef.sdp.exception.BookingNotFoundException;
import com.klef.sdp.exception.InsufficientSeatsException;
import com.klef.sdp.repository.BookingRepository;
import com.klef.sdp.repository.EventInventoryRepository;
import com.klef.sdp.repository.SeatHoldRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingrepo;

    @Autowired
    private EventInventoryRepository eventrepo;

    @Autowired
    private SeatHoldRepository seatholdrepo;

    @Override
    public AvailabilityResponse checkAvailability(int eventId) {
        EventInventory inventory = eventrepo
                                    .findById(eventId)
                                    .orElseThrow(() -> 
                                    new RuntimeException("Event inventory not found"));

        return AvailabilityResponse.builder()
                .eventId(inventory.getEventId())
                .totalSeats(inventory.getTotalSeats())
                .availableSeats(inventory.getAvailableSeats())
                .heldSeats(inventory.getHeldSeats())
                .bookedSeats(inventory.getBookedSeats())
                .build();
    }

    @Override
    @Transactional 
    public BookingResponse holdSeats(HoldRequest request) {
        //atomically hold the requested seats 
        int updatedRows = eventrepo.holdSeats(
            request.getEventId(),
            request.getSeatCount()
        );

        //if no rows were updated, seats are unavailable
        if (updatedRows == 0) {
            throw new InsufficientSeatsException("Not enought seats are available");
        }

        //create the booking 
        Booking booking = Booking.builder()
            .userId(request.getUserId())
            .eventId(request.getEventId())
            .seatsBooked(request.getSeatCount())
            .bookingStatus(BookingStatus.HELD)
            .build();
        
        booking = bookingrepo.save(booking);

        //create the temporary hold
        SeatHold seatHold = SeatHold.builder()
            .bookingId(booking.getBookingId())
            .eventId(request.getEventId())
            .userId(request.getUserId())
            .seatCount(request.getSeatCount())
            .expiresAt(LocalDateTime.now().plusMinutes(5))
            .holdStatus(HoldStatus.ACTIVE)
            .build(); 

        seatholdrepo.save(seatHold);

        return  BookingResponse.builder()
            .bookingId(booking.getBookingId())
            .eventId(booking.getEventId())
            .userId(booking.getUserId())
            .seatsBooked(booking.getSeatsBooked())
            .bookingStatus(booking.getBookingStatus())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }

    @Override
    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {

        EventInventory inventory = EventInventory.builder()
                .eventId(request.getEventId())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())
                .heldSeats(0)
                .bookedSeats(0)
                .build();

        inventory = eventrepo.save(inventory);

        return InventoryResponse.builder()
                .eventId(inventory.getEventId())
                .totalSeats(inventory.getTotalSeats())
                .availableSeats(inventory.getAvailableSeats())
                .heldSeats(inventory.getHeldSeats())
                .bookedSeats(inventory.getBookedSeats())
                .build();
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(int bookingId) {
        Booking booking = bookingrepo.findById(bookingId)
            .orElseThrow(() -> 
                new BookingNotFoundException("Booking Not Found"));

        if (booking.getBookingStatus() != BookingStatus.HELD){
            throw new RuntimeException("Booking not in HELD state.");
        }

        SeatHold seatHold = seatholdrepo.findByBookingIdAndHoldStatus(bookingId, HoldStatus.ACTIVE)
            .orElseThrow(() ->
                new RuntimeException("Active seat hold not found"));
        
        seatHold.setHoldStatus(HoldStatus.CONVERTED);
        seatholdrepo.save(seatHold);

        EventInventory inventory = eventrepo.findById(booking.getEventId())
            .orElseThrow( () -> 
                new RuntimeException("Event Inventory not found"));

        inventory.setHeldSeats(inventory.getHeldSeats() - booking.getSeatsBooked());

        inventory.setBookedSeats(inventory.getBookedSeats() + booking.getSeatsBooked());

        eventrepo.save(inventory);

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        booking = bookingrepo.save(booking);

        return BookingResponse.builder()
            .bookingId(booking.getBookingId())
            .eventId(booking.getEventId())
            .userId(booking.getBookingId())
            .seatsBooked(booking.getSeatsBooked())
            .bookingStatus(booking.getBookingStatus())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build(); 
    }

    @Override
    public BookingResponse cancelBooking(int bookingId) {
        Booking booking = bookingrepo.findById(bookingId)
            .orElseThrow(() -> 
                new BookingNotFoundException("Booking not found."));
        
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled.");
        }

        EventInventory inventory = eventrepo.findById(booking.getEventId())
            .orElseThrow(() -> 
                new RuntimeException("Event Inventory not found")); 
        
        int seats = booking.getSeatsBooked();

        if (booking.getBookingStatus() == BookingStatus.HELD) {
            inventory.setHeldSeats(inventory.getHeldSeats() - seats);
        } else if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            inventory.setBookedSeats(inventory.getBookedSeats() - seats);
        } else {
            throw new RuntimeException("Only HELD or CONFIRMED bookings can be cancelled");
        }

        inventory.setAvailableSeats(inventory.getAvailableSeats() + seats);

        eventrepo.save(inventory);

        booking.setBookingStatus(BookingStatus.CANCELLED);

        booking = bookingrepo.save(booking);

        return BookingResponse.builder()
            .bookingId(booking.getBookingId())
            .eventId(booking.getEventId())
            .userId(booking.getUserId())
            .seatsBooked(booking.getSeatsBooked())
            .bookingStatus(booking.getBookingStatus())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build();
    }

    @Override
    public BookingResponse getBookingById(int bookingId) {
        Booking booking = bookingrepo.findById(bookingId)
            .orElseThrow(() -> 
                new BookingNotFoundException("Booking Not Found."));
        
        return BookingResponse.builder()
            .bookingId(booking.getBookingId())
            .eventId(booking.getEventId())
            .userId(booking.getUserId())
            .seatsBooked(booking.getSeatsBooked())
            .bookingStatus(booking.getBookingStatus())
            .createdAt(booking.getCreatedAt())
            .updatedAt(booking.getUpdatedAt())
            .build(); 
    } 

    @Override 
    @Transactional 
    public void releaseExpiredHolds() {
        List<SeatHold> expiredHold = seatholdrepo
            .findByHoldStatusAndExpiresAtBefore(HoldStatus.ACTIVE, LocalDateTime.now()); 
        
        for (SeatHold hold : expiredHold) {
            EventInventory inventory = eventrepo.findById(hold.getEventId())
                .orElseThrow(() -> 
                    new RuntimeException("Event Inventory not found."));

            inventory.setHeldSeats(inventory.getHeldSeats() - hold.getSeatCount());

            inventory.setAvailableSeats(inventory.getAvailableSeats() + hold.getSeatCount());

            eventrepo.save(inventory);

            hold.setHoldStatus(HoldStatus.EXPIRED);
            seatholdrepo.save(hold); 

            Booking booking = bookingrepo.findById(hold.getBookingId())
                .orElseThrow(() -> 
                    new BookingNotFoundException("Booking not found."));

            booking.setBookingStatus(BookingStatus.EXPIRED);
            bookingrepo.save(booking);
        }
    } 
}
