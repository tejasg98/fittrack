package com.appventory.fittrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;

public interface ActivityLogService {
	ActivityLogResponse createActivityLog(ActivityLogRequest request);

	Page<ActivityLogResponse> getAllActivityLogs(Pageable pageable);

	ActivityLogResponse getActivityLogById(Long id);

	ActivityLogResponse updateActivityLog(Long id, ActivityLogRequest request);

	String deleteActivityLog(Long id);
}
