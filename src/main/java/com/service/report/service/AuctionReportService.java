package com.service.report.service;

import com.service.report.domain.AuctionReport;
import com.service.report.dto.AuctionReportRequest;
import com.service.report.exception.InvalidReportRequestException;
import com.service.report.exception.ReportNotFoundException;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.AuctionReported;
import com.service.report.metrics.ReportMetrics;
import com.service.report.repository.AuctionReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionReportService {
    private final AuctionReportRepository auctionReportRepository;
    private final KafkaService kafkaService;
    private final ReportMetrics reportMetrics;

    @Transactional
    public AuctionReport reportAuction(AuctionReportRequest request) {
        validateRequest(request);
        long start = System.currentTimeMillis();
        try {
            log.info("Iniciando report de leilão. auctionId={} | sellerId={} | userId={}", request.auctionId(), request.sellerId(), request.userId());
            AuctionReport report = new AuctionReport(request.auctionId(), request.sellerId(), request.reportReason());
            AuctionReport saved;
            try {
                saved = auctionReportRepository.save(report);
            } catch (Exception e) {
                reportMetrics.incrementPersistFailure("auction");
                throw e;
            }
            reportMetrics.incrementCreated("auction");
            log.info("Report de leilão persistido com sucesso. reportId={} | auctionId={} | sellerId={}", saved.getId(), request.auctionId(), request.sellerId());
            AuctionReported event = new AuctionReported(request.userId(), request.auctionId(), request.sellerId(), request.auctionTitle(), request.auctionDescription(), request.reportReason(), Instant.now(), request.auctionThumb(), UUID.randomUUID());
            try {
                kafkaService.sendEvent(event);
                reportMetrics.incrementPublishSuccess("auction");
            } catch (Exception e) {
                reportMetrics.incrementPublishFailure("auction");
                throw e;
            }
            reportMetrics.recordProcessingTime("auction", "create_report", "success", System.currentTimeMillis() - start);
            log.info("Report de leilão concluído. reportId={} | auctionId={} | sellerId={}", saved.getId(), request.auctionId(), request.sellerId());
            return saved;
        } catch (Exception e) {
            reportMetrics.recordProcessingTime("auction", "create_report", "failure", System.currentTimeMillis() - start);
            throw e;
        }
    }

    @Transactional
    public AuctionReport setAuctionReportApproved(AuctionReported event) {
        long start = System.currentTimeMillis();
        try {
            log.info("Processando aprovação de report de leilão. auctionId={}", event.auctionId());
            AuctionReport report = auctionReportRepository.findById(event.auctionId()).orElseThrow(() -> {
                reportMetrics.incrementNotFound("auction");
                log.error("AuctionReport não encontrado. auctionId={}", event.auctionId());
                return new ReportNotFoundException("AuctionReport not found for auctionId: " + event.auctionId());
            });
            report.setReportApproved(true);
            AuctionReport saved;
            try {
                saved = auctionReportRepository.save(report);
            } catch (Exception e) {
                reportMetrics.incrementPersistFailure("auction");
                throw e;
            }
            reportMetrics.incrementApproved("auction");
            reportMetrics.recordProcessingTime("auction", "approve_report", "success", System.currentTimeMillis() - start);
            log.info("Report de leilão marcado como aprovado com sucesso. reportId={} | auctionId={}", saved.getId(), event.auctionId());
            return saved;
        } catch (Exception e) {
            reportMetrics.recordProcessingTime("auction", "approve_report", "failure", System.currentTimeMillis() - start);
            throw e;
        }
    }

    private void validateRequest(AuctionReportRequest request) {
        if (request == null) {
            throw new InvalidReportRequestException("Auction report request is required");
        }
        if (request.userId() == null) {
            throw new InvalidReportRequestException("userId is required");
        }
        if (request.auctionId() == null) {
            throw new InvalidReportRequestException("auctionId is required");
        }
        if (request.sellerId() == null) {
            throw new InvalidReportRequestException("sellerId is required");
        }
        if (request.reportReason() == null || request.reportReason().isBlank()) {
            throw new InvalidReportRequestException("reportReason is required");
        }
    }
}