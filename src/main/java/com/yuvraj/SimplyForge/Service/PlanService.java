package com.yuvraj.SimplyForge.Service;

import java.util.List;

import com.yuvraj.SimplyForge.dto.subscription.PlanResponse;

public interface PlanService {

    public List<PlanResponse> getAllActivePlans();
}
