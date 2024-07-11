package com.dvargas42.planner_backend.module.link;

import java.util.UUID;

public record LinkData(
    UUID id,
    String title,
    String url
) {
}
