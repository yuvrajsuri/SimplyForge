package com.yuvraj.SimplyForge.Service;

import com.yuvraj.SimplyForge.dto.subscription.CheckoutRequest;
import com.yuvraj.SimplyForge.dto.subscription.CheckoutResponse;
import com.yuvraj.SimplyForge.dto.subscription.PortalResponse;
import com.yuvraj.SimplyForge.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {

    public SubscriptionResponse getCurrentSubscription(Long userId);

    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    public PortalResponse openCustomerPortal(Long userId);

}
