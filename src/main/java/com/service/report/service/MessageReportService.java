package com.service.report.service;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.MessageReported;
import com.service.report.repository.MessageReportRepository;
import jakarta.persistence.EntityNotFoundException;
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
                    return new EntityNotFoundException(
                            "MessageReport not found for messageId: " + MessageReported.messageId()
                    );
                });

        report.setReportApproved(true);

        MessageReport saved = messageReportRepository.save(report);
        log.info("Report de mensagem marcado como aprovado com sucesso. reportId={} | messageId={}",
                saved.getId(), MessageReported.messageId());

        return saved;
    }
}
