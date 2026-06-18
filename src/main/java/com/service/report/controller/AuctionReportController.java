package com.service.report.controller;

import com.service.report.domain.AuctionReport;
import com.service.report.dto.AuctionReportRequest;
import com.service.report.exception.InvalidReportRequestException;
import com.service.report.service.AuctionReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report-auction")
@RequiredArgsConstructor
public class AuctionReportController {
    private final AuctionReportService auctionReportService;

    @PostMapping
    public ResponseEntity<AuctionReport> save(@RequestBody AuctionReportRequest request) {
        if (request == null) {
            throw new InvalidReportRequestException("Auction report request is required");
        }

        log.info("Recebendo requisição de report de leilão. auctionId={} | sellerId={} | userId={}",
                request.auctionId(), request.sellerId(), request.userId());

        AuctionReport saved = auctionReportService.reportAuction(request);

        log.info("Report de leilão criado com sucesso. reportId={} | auctionId={} | sellerId={}",
                saved.getId(), request.auctionId(), request.sellerId());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
