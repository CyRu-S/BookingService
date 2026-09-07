package com.klef.sdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klef.sdp.dto.AvailabilityResponse;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.dto.HoldRequest;
import com.klef.sdp.dto.InventoryRequest;
import com.klef.sdp.dto.InventoryResponse;
import com.klef.sdp.service.BookingService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/bookings")
public class BookingController {
    
    @Autowired 
    private BookingService bookingService;

    @GetMapping("/")
    public String home() {
        return "Booking Service is Working.";
    }

    @GetMapping("/displayBooking/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));  
    }

    @GetMapping("/availability/{eventId}") 
    public ResponseEntity<AvailabilityResponse> checkAvailability(@Valid @PathVariable int eventId) {
        return ResponseEntity.ok(bookingService.checkAvailability(eventId));
    }

    @PostMapping("/hold")
    public ResponseEntity<BookingResponse> holdSeats(@Valid @RequestBody HoldRequest request) {
        return ResponseEntity.ok(bookingService.holdSeats(request));
    }

    @PostMapping("/inventory")
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(bookingService.createInventory(request)); 
    }

    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<BookingResponse> confirmBooking (@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId)); 
    } 

    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}
