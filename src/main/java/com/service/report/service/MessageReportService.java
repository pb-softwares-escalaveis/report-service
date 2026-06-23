package com.service.report.service;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.exception.InvalidReportRequestException;
import com.service.report.exception.ReportNotFoundException;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.MessageReported;
import com.service.report.metrics.ReportMetrics;
import com.service.report.repository.MessageReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReportService {
    private final MessageReportRepository messageReportRepository;
    private final KafkaService kafkaService;
    private final ReportMetrics reportMetrics;

    @Transactional
    public MessageReport reportMessage(MessageReportRequest request) {
        validateRequest(request);
        long start = System.currentTimeMillis();
        try {
            log.info(
                    "Iniciando report de mensagem. messageId={} | auctionId={} | sellerId={} | userId={}",
                    request.messageId(),
                    request.auctionId(),
                    request.sellerId(),
                    request.userId()
            );
            MessageReport report = new MessageReport(
                    null,
                    request.messageId(),
                    request.userId(),
                    request.reportReason()
            );
            MessageReport saved;
            try {
                saved = messageReportRepository.save(report);
            } catch (Exception e) {
                reportMetrics.incrementPersistFailure(
                        "message"
                );
                throw e;
            }
            reportMetrics.incrementCreated(
                    "message"
            );
            MessageReported event = new MessageReported(
                    request.userId(),
                    request.sellerId(),
                    request.auctionId(),
                    request.messageId(),
                    request.message(),
                    request.reportReason(),
                    Instant.now(),
                    UUID.randomUUID()
            );
            try {
                kafkaService.sendEvent(event);
                reportMetrics.incrementPublishSuccess(
                        "message"
                );
            } catch (Exception e) {
                reportMetrics.incrementPublishFailure(
                        "message"
                );
                throw e;
            }
            reportMetrics.recordProcessingTime(
                    "message",
                    "create_report",
                    "success",
                    System.currentTimeMillis() - start
            );
            log.info(
                    "Report de mensagem concluído. reportId={} | messageId={} | auctionId={}",
                    saved.getId(),
                    request.messageId(),
                    request.auctionId()
            );
            return saved;
        } catch (Exception e) {
            reportMetrics.recordProcessingTime(
                    "message",
                    "create_report",
                    "failure",
                    System.currentTimeMillis() - start
            );
            throw e;
        }
    }

    @Transactional
    public MessageReport setMessageReportApproved(MessageReported event) {
        long start = System.currentTimeMillis();
        try {
            MessageReport report = messageReportRepository
                    .findById(event.messageId())
                    .orElseThrow(() -> {
                        reportMetrics.incrementNotFound(
                                "message"
                        );
                        return new ReportNotFoundException(
                                "MessageReport not found for messageId: "
                                        + event.messageId()
                        );
                    });
            report.setReportApproved(true);
            MessageReport saved;
            try {
                saved = messageReportRepository.save(report);
            } catch (Exception e) {
                reportMetrics.incrementPersistFailure(
                        "message"
                );
                throw e;
            }
            reportMetrics.incrementApproved(
                    "message"
            );
            reportMetrics.recordProcessingTime(
                    "message",
                    "approve_report",
                    "success",
                    System.currentTimeMillis() - start
            );
            return saved;
        } catch (Exception e) {
            reportMetrics.recordProcessingTime(
                    "message",
                    "approve_report",
                    "failure",
                    System.currentTimeMillis() - start
            );
            throw e;
        }
    }

    private void validateRequest(MessageReportRequest request) {
        if (request == null) {
            throw new InvalidReportRequestException(
                    "Message report request is required"
            );
        }
        if (request.userId() == null) {
            throw new InvalidReportRequestException(
                    "userId is required"
            );
        }
        if (request.auctionId() == null) {
            throw new InvalidReportRequestException(
                    "auctionId is required"
            );
        }
        if (request.sellerId() == null) {
            throw new InvalidReportRequestException(
                    "sellerId is required"
            );
        }
        if (request.messageId() == null) {
            throw new InvalidReportRequestException(
                    "messageId is required"
            );
        }
        if (request.reportReason() == null ||
                request.reportReason().isBlank()) {
            throw new InvalidReportRequestException(
                    "reportReason is required"
            );
        }
    }
}