package com.codehows.taelim.repository;

import com.codehows.taelim.entity.EmailSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailSetRepository extends JpaRepository<EmailSet, Long> {

    List<EmailSet> findAll();

    Optional<EmailSet> findByIsSelectedTrue();

}
