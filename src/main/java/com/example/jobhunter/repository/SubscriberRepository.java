package com.example.jobhunter.repository;

import com.example.jobhunter.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber,Long> {
    Boolean existsByEmail(String email);
    Subscriber findById(long id);
}
