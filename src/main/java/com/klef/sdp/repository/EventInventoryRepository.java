package com.klef.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.klef.sdp.entity.EventInventory;

import feign.Param;

public interface EventInventoryRepository extends JpaRepository<EventInventory, Integer> {
    
    @Modifying 
    @Query ("""
            UPDATE EventInventory e 
            SET e.availableSeats = e.availableSeats - :seatCount,
                e.heldSeats = e.heldSeats + :seatCount
            WHERE e.eventId = :eventId
            AND e.availableSeats > :seatCount
            """)
    int holdSeats(
        @Param("eventId") int eventId,
        @Param("seatCount") int seatCount
    );
}
