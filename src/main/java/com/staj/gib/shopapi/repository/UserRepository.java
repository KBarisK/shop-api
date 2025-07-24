package com.staj.gib.shopapi.repository;

import com.staj.gib.shopapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}