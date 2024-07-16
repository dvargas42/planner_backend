package com.dvargas42.planner_backend.module.participant.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ParticipantConfirmReqDTO(

    @NotBlank
    @Pattern(regexp = "^$|.*\\s.*", message = "must not have one word")
    @Pattern(regexp = "^([A-Za-zÀ-ÖØ-öø-ÿ0-9\\s]*)$", message = "must not have special characters")
    @Length(max = 255, message = "must not have more than 255 characters")
    String name,

    @NotBlank
    @Email(message = "must have an valid e-mail")
    @Length(max = 255, message = "must not have more than 255 characters")
    String email
) {}
