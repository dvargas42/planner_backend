package com.dvargas42.planner_backend.module.activity.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ActivityCreateReqDTO(

    @NotNull
    UUID trip_id,

    @NotBlank
    @Pattern(regexp = "^$|.*\\s.*", message = "must not have one word")
    @Pattern(regexp = "^([A-Za-zÀ-ÖØ-öø-ÿ0-9\\s]*)$", message = "must not have special characters")
    @Length(max = 255, message = "must not have more than 255 characters")
    String title,

    @NotBlank
    String occurs_at
){}
