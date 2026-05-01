package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import com.yuvraj.SimplyForge.Enums.SubscriptionStatus;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {
    Long id;
    User user;
    Plan plan;

    SubscriptionStatus status;

    String stripeCustomerId;
    String stripeSubscriptionid;

    Instant currentPeriodStart;
    Instant currentPeriodEnd;

    Boolean CancelAtPeriodEnd;
    Instant createdAt;
    Instant updatedAt;
}
