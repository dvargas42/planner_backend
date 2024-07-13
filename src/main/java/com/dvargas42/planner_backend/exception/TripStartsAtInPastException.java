package com.dvargas42.planner_backend.exception;

public class TripStartsAtInPastException extends CustomException {
    
    public TripStartsAtInPastException(String... searchParamsMap) {
        super("Trip starts_at in past", searchParamsMap);
    }
}
