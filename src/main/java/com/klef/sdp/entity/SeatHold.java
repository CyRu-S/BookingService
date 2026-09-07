package com.klef.sdp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity 
@Table (name = "seat_hold") 
public class SeatHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int holdId;

    @Column(name="booking_id", nullable=false)
    private int bookingId;

    @Column(name="event_id", nullable=false)
    private int eventId;

    @Column(name="user_id", nullable=false)
    private int userId;
    
    @Column(name="seat_count", nullable=false)
    private int seatCount;

    @Column(name="expires_at", nullable=false)
    private LocalDateTime expiresAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HoldStatus holdStatus;
    
    @CreationTimestamp 
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

}
