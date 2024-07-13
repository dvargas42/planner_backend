package com.dvargas42.planner_backend.module.participant.dto;

import java.util.UUID;

import com.dvargas42.planner_backend.module.participant.Participant;

public record ParticipantConfirmRespDTO(

    UUID id,
    String name,
    String email,
    Boolean is_confirmed
) {
    public ParticipantConfirmRespDTO(Participant participant) {
        this(
            participant.getId(), 
            participant.getName(), 
            participant.getEmail(), 
            participant.getIsConfirmed()
        );
    }
}
