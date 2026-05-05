package com.yuvraj.SimplyForge.dto.subscription;

public record PlanLimitResponse(
    String planName,
    Integer maxTokensPerDay,
    Integer maxProjects,
    boolean unlimitedAi
) {

}
