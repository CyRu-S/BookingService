package com.klef.sdp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class InventoryResponse {
    private Integer eventId;
    private int totalSeats;
    private int availableSeats;
    private int heldSeats;
    private int bookedSeats;
}
