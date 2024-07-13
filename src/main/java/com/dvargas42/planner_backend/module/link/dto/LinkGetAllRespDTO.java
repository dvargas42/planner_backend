package com.dvargas42.planner_backend.module.link.dto;

import java.util.UUID;

public record LinkGetAllRespDTO(
    UUID id,
    String title,
    String url
) {
}
