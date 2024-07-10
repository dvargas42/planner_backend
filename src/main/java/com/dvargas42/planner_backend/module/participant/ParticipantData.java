package com.dvargas42.planner_backend.module.participant;

import java.util.UUID;

public record ParticipantData(
        UUID id,
        String name,
        String email,
        Boolean isConfirmed
) {}
