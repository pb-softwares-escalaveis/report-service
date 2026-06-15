package com.service.report.repository;

import com.service.report.domain.MessageReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReportRepository extends JpaRepository<MessageReport, Long> {
}
