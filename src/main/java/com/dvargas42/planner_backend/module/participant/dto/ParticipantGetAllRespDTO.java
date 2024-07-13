package com.dvargas42.planner_backend.module.participant.dto;

import java.util.UUID;

import com.dvargas42.planner_backend.module.participant.Participant;

public record ParticipantGetAllRespDTO(

    UUID id,
    String name,
    String email,
    Boolean is_confirmed
) {
    public ParticipantGetAllRespDTO(Participant participant) {
        this(
            participant.getId(), 
            participant.getName(), 
            participant.getEmail(), 
            participant.getIsConfirmed()
        );
    }
}
