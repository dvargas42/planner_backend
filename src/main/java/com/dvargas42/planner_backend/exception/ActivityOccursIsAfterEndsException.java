package com.dvargas42.planner_backend.exception;

public class ActivityOccursIsAfterEndsException extends CustomException {
    
    public ActivityOccursIsAfterEndsException(String... searchParamsMap) {
        super("ocurrs_at is after ends_at", searchParamsMap);
    }
}
