package com.klef.sdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component 
public class ExpiredHoldScheduler {
    
    @Autowired 
    private BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void releaseExpiredHolds() {
        bookingService.releaseExpiredHolds();
    }
} 
