package com.klef.sdp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class HoldRequest {

    @NotNull(message = "Event ID is required")
    @Positive(message = "Event ID must be greater than 0")
    private Integer eventId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    private Integer userId;

    @NotNull(message = "Seat count is required")
    @Positive(message = "Seat count must be greater than 0")
    private Integer seatCount;
}
