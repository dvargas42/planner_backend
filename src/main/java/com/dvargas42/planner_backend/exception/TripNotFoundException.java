package com.dvargas42.planner_backend.exception;

public class TripNotFoundException extends CustomException {
    
    public TripNotFoundException(String... searchParamsMap) {
        super("Trip not found", searchParamsMap);
    }
}
