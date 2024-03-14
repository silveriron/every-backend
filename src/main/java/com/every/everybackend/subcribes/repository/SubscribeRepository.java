package com.every.everybackend.subcribes.repository;

import com.every.everybackend.subcribes.entity.SubscribeEntity;
import com.every.everybackend.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
    void deleteByUserAndAuthor(UserEntity user, UserEntity author);
}
