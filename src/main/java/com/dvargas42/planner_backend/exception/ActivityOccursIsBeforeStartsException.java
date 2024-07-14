package com.dvargas42.planner_backend.exception;

public class ActivityOccursIsBeforeStartsException extends CustomException {
    
    public ActivityOccursIsBeforeStartsException(String... searchParamsMap) {
        super("ocurrs_at is before starts_at", searchParamsMap);
    }
}
