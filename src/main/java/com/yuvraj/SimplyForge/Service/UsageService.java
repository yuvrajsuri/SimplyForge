package com.yuvraj.SimplyForge.Service;

import com.yuvraj.SimplyForge.dto.subscription.PlanLimitResponse;
import com.yuvraj.SimplyForge.dto.subscription.UsageTodayResponse;

public interface UsageService {

    public UsageTodayResponse getTodayUsageOfUser(Long userId);

    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
