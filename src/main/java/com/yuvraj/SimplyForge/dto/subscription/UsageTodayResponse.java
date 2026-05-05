package com.yuvraj.SimplyForge.dto.subscription;

public record UsageTodayResponse(
    Integer tokensUsed,
    Integer tokenLimit,
    Integer previewsRunning,
    Integer previewsLimit
) {

}
