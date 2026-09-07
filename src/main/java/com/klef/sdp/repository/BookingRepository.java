package com.klef.sdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klef.sdp.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    
}
