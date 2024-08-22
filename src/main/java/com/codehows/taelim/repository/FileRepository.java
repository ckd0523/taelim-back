package com.codehows.taelim.repository;

import com.codehows.taelim.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
