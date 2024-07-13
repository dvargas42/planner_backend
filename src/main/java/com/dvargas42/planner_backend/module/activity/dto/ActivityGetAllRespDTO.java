package com.dvargas42.planner_backend.module.activity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dvargas42.planner_backend.module.activity.Activity;

public record ActivityGetAllRespDTO(
    UUID id, 
    String title, 
    LocalDateTime occurs_at
) {
    public ActivityGetAllRespDTO(Activity activity) {
        this(activity.getId(), activity.getTitle(), activity.getOccursAt());
    }
}
