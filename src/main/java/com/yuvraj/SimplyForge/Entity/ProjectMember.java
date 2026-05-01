package com.yuvraj.SimplyForge.Entity;

import java.time.Instant;

import com.yuvraj.SimplyForge.Enums.ProjectRole;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level=AccessLevel.PRIVATE)
public class ProjectMember {
    ProjectMemberId id;
    Project project;
    User user;

    ProjectRole projectRole;

    Instant invitedAt;
    Instant acceptedAt;
}
