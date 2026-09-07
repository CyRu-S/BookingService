package com.klef.sdp.service;

import com.klef.sdp.dto.AvailabilityResponse;
import com.klef.sdp.dto.BookingResponse;
import com.klef.sdp.dto.HoldRequest;
import com.klef.sdp.dto.InventoryRequest;
import com.klef.sdp.dto.InventoryResponse;

public interface BookingService {
    AvailabilityResponse checkAvailability(int eventId);

    BookingResponse holdSeats(HoldRequest request);

    InventoryResponse createInventory(InventoryRequest request);

    BookingResponse confirmBooking(int bookingId);

    BookingResponse cancelBooking(int bookingId);

    BookingResponse getBookingById(int bookingId);

    void releaseExpiredHolds(); 
}
