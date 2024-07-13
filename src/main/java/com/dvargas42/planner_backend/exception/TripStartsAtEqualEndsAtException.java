package com.dvargas42.planner_backend.exception;

public class TripStartsAtEqualEndsAtException extends CustomException {
    
    public TripStartsAtEqualEndsAtException(String... searchParamsMap) {
        super("Trip starts_at is equal ends_at", searchParamsMap);
    }
}
