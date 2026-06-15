package com.service.report.service;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.MessageReportApproved;
import com.service.report.kafka.events.MessageReportedPendingReview;
import com.service.report.repository.MessageReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageReportService {

    private final MessageReportRepository messageReportRepository;
    private final KafkaService kafkaService;

    @Transactional
    public MessageReport reportMessage(MessageReportRequest request) {
        MessageReport report = new MessageReport(
                null,
                request.messageId(),
                request.userId(),
                request.reportReason()
        );

        MessageReport saved = messageReportRepository.save(report);

        MessageReportedPendingReview event = new MessageReportedPendingReview(
                request.userId(),
                request.auctionId(),
                request.sellerId(),
                request.messageId(),
                request.message(),
                request.reportReason(),
                request.ocurredAt(),
                request.correlationId()
        );

        kafkaService.sendEvent(event);

        return saved;
    }

    public MessageReport setMessageReportApproved(MessageReportApproved messageReportApproved) {
        MessageReport report = messageReportRepository.findById(messageReportApproved.messageId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "MessageReport not found for messageId: " + messageReportApproved.messageId()
                ));

        report.setReportApproved(true);

        return messageReportRepository.save(report);
    }
}