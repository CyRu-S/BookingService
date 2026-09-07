package com.klef.sdp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class InventoryRequest {

    @NotNull(message = "Event ID is required")
    @Positive(message = "Event ID must be greater than 0")
    private Integer eventId;

    @NotNull(message = "Total seats is required") 
    @Positive(message = "Total seats must be greater than 0")
    private Integer totalSeats;
}
