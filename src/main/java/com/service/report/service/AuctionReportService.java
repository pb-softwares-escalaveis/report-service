package com.service.report.service;

import com.service.report.domain.AuctionReport;
import com.service.report.dto.AuctionReportRequest;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.AuctionReportApproved;
import com.service.report.kafka.events.AuctionReportedPendingReview;
import com.service.report.repository.AuctionReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionReportService {

    private final AuctionReportRepository auctionReportRepository;
    private final KafkaService kafkaService;

    @Transactional
    public AuctionReport reportAuction(AuctionReportRequest request) {
        AuctionReport report = new AuctionReport(
                request.auctionId(),
                request.sellerId(),
                request.reportReason()
        );

        AuctionReport saved = auctionReportRepository.save(report);

        AuctionReportedPendingReview event = new AuctionReportedPendingReview(
                request.userId(),
                request.auctionId(),
                request.sellerId(),
                request.auctionTitle(),
                request.auctionDescription(),
                request.reportReason(),
                request.ocurredAt(),
                request.auctionThumb(),
                request.correlationId()
        );

        kafkaService.sendEvent(event);

        return saved;
    }

    public AuctionReport setAuctionReportApproved(AuctionReportApproved auctionReportApproved) {
        AuctionReport report = auctionReportRepository.findById(auctionReportApproved.auctionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "AuctionReport not found for auctionId: " + auctionReportApproved.auctionId()
                ));

        report.setReportApproved(true);

        return auctionReportRepository.save(report);
    }
}