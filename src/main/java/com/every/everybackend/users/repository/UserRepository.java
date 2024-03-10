package com.every.everybackend.users.repository;

import com.every.everybackend.users.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

  public Optional<UserEntity> findByEmail(String email);
}
