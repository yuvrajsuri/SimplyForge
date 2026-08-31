package com.yuvraj.SimplyForge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuvraj.SimplyForge.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

