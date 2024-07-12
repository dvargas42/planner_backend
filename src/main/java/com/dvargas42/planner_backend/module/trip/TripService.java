package com.dvargas42.planner_backend.module.trip;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvargas42.planner_backend.exception.TripNotFoundException;

@Service
public class TripService {
    
    @Autowired
    private TripRepository tripRepository;

    public Trip findTrip(UUID id) {
        return this.tripRepository.findById(id)
            .orElseThrow(() -> {
                throw new TripNotFoundException(Trip.class, "id", id.toString());
            });

    }
}
