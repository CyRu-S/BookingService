package com.klef.sdp.dto;

import java.time.LocalDateTime;

import com.klef.sdp.entity.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class BookingResponse {
    private int bookingId;
    private int eventId;
    private int userId;
    private int seatsBooked;
    private BookingStatus bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
