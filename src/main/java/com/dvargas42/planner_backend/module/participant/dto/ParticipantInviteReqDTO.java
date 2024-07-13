package com.dvargas42.planner_backend.module.participant.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ParticipantInviteReqDTO(

    @NotNull
    UUID trip_id,

    @Email(message = "must have an valid e-mail")
    @Length(max = 255, message = "must not have more than 255 characters")
    String email
) {
}
