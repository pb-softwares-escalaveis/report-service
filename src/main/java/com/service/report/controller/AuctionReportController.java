package com.service.report.controller;

import com.service.report.domain.AuctionReport;
import com.service.report.dto.AuctionReportRequest;
import com.service.report.service.AuctionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report-auction")
@RequiredArgsConstructor
public class AuctionReportController {
    private final AuctionReportService auctionReportService;

    @PostMapping
    public ResponseEntity<AuctionReport> save(@RequestBody AuctionReportRequest request) {
        AuctionReport saved = auctionReportService.reportAuction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}