package com.dvargas42.planner_backend.exception;

public class TripEndsAtIsNotMoreThanStartsAtException extends CustomException {
    
    public TripEndsAtIsNotMoreThanStartsAtException(String... searchParamsMap) {
        super("Trip ends_at is not more than starts_at", searchParamsMap);
    }
}

