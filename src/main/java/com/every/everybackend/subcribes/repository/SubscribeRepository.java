package com.every.everybackend.subcribes.repository;

import com.every.everybackend.subcribes.entity.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, Long> {
}
