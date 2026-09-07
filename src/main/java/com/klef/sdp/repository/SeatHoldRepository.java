package com.klef.sdp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.sdp.entity.HoldStatus;
import com.klef.sdp.entity.SeatHold;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Integer> {
    List<SeatHold> findByHoldStatusAndExpiresAtBefore(
        HoldStatus holdStatus,
        LocalDateTime currentTime
    );

    Optional<SeatHold> findByBookingIdAndHoldStatus(
        int bookingId,
        HoldStatus holdStatus
    );
}
