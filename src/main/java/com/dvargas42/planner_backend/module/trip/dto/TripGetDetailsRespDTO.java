package com.dvargas42.planner_backend.module.trip.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dvargas42.planner_backend.module.trip.Trip;

public record TripGetDetailsRespDTO(

    UUID id,
    String destination,
    LocalDateTime starts_at,
    LocalDateTime ends_at,
    Boolean is_confirmed,
    String owner_name,
    String owner_email
) { 
    public TripGetDetailsRespDTO(Trip trip) { 
        this(
            trip.getId(), 
            trip.getDestination(),
            trip.getStartsAt(),
            trip.getEndsAt(),
            trip.getIsConfirmed(),
            trip.getOwnerName(),
            trip.getOwnerEmail());
    }
}
