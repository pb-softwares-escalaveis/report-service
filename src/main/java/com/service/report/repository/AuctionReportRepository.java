package com.service.report.repository;

import com.service.report.domain.AuctionReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionReportRepository extends JpaRepository<AuctionReport, Long> {
}
