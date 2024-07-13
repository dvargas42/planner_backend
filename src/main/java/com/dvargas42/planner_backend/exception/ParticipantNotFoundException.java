package com.dvargas42.planner_backend.exception;

public class ParticipantNotFoundException extends CustomException {
    
    public ParticipantNotFoundException(String... searchParamsMap) {
        super("Participant not found", searchParamsMap);
    }
}
