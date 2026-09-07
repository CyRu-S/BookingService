package com.klef.sdp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class AvailabilityResponse {

    private int eventId;
    private int totalSeats;
    private int availableSeats;
    private int heldSeats;
    private int bookedSeats;
    
}
