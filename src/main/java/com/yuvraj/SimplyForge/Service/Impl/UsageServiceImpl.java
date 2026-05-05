package com.yuvraj.SimplyForge.Service.Impl;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.UsageService;
import com.yuvraj.SimplyForge.dto.subscription.PlanLimitResponse;
import com.yuvraj.SimplyForge.dto.subscription.UsageTodayResponse;

@Service
public class UsageServiceImpl implements UsageService{

    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodayUsageOfUser'");
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSubscriptionLimitsOfUser'");
    }

}
