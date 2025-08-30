package com.appventory.fittrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appventory.fittrack.model.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	   List<ActivityLog> findByUserId(Long userId);
}