package com.service.report.service;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.exception.InvalidReportRequestException;
import com.service.report.exception.ReportNotFoundException;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.MessageReported;
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

    @Transactional
    public MessageReport reportMessage(MessageReportRequest request) {
        validateRequest(request);

        log.info("Iniciando report de mensagem. messageId={} | auctionId={} | sellerId={} | userId={}",
                request.messageId(), request.auctionId(), request.sellerId(), request.userId());

        MessageReport report = new MessageReport(
                null,
                request.messageId(),
                request.userId(),
                request.reportReason()
        );

        MessageReport saved = messageReportRepository.save(report);
        log.info("Report de mensagem persistido com sucesso. reportId={} | messageId={} | auctionId={}",
                saved.getId(), request.messageId(), request.auctionId());

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

        log.debug("Publicando evento MessageReportedPendingReview no Kafka. messageId={} | auctionId={} | correlationId={}",
                request.messageId(), request.auctionId(), event.correlationId());

        kafkaService.sendEvent(event);

        log.info("Report de mensagem concluído. reportId={} | messageId={} | auctionId={}",
                saved.getId(), request.messageId(), request.auctionId());

        return saved;
    }

    public MessageReport setMessageReportApproved(MessageReported MessageReported) {
        log.info("Processando aprovação de report de mensagem. messageId={}",
                MessageReported.messageId());

        MessageReport report = messageReportRepository.findById(MessageReported.messageId())
                .orElseThrow(() -> {
                    log.error("MessageReport não encontrado. messageId={}", MessageReported.messageId());
                    return new ReportNotFoundException(
                            "MessageReport not found for messageId: " + MessageReported.messageId()
                    );
                });

        report.setReportApproved(true);

        MessageReport saved = messageReportRepository.save(report);
        log.info("Report de mensagem marcado como aprovado com sucesso. reportId={} | messageId={}",
                saved.getId(), MessageReported.messageId());

        return saved;
    }

    private void validateRequest(MessageReportRequest request) {
        if (request == null) {
            throw new InvalidReportRequestException("Message report request is required");
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
        if (request.messageId() == null) {
            throw new InvalidReportRequestException("messageId is required");
        }
        if (request.reportReason() == null || request.reportReason().isBlank()) {
            throw new InvalidReportRequestException("reportReason is required");
        }
    }
}
