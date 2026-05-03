package com.yuvraj.SimplyForge.Service.Impl;

import org.springframework.stereotype.Service;

import com.yuvraj.SimplyForge.Service.SubscriptionService;
import com.yuvraj.SimplyForge.dto.subscription.CheckoutRequest;
import com.yuvraj.SimplyForge.dto.subscription.CheckoutResponse;
import com.yuvraj.SimplyForge.dto.subscription.PortalResponse;
import com.yuvraj.SimplyForge.dto.subscription.SubscriptionResponse;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSubscription'");
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createCheckoutSessionUrl'");
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openCustomerPortal'");
    }

}
