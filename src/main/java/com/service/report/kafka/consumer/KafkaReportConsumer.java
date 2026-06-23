package com.service.report.kafka.consumer;

import com.service.report.kafka.events.AuctionReported;
import com.service.report.kafka.events.MessageReported;
import com.service.report.metrics.ConsumerMetrics;
import com.service.report.service.AuctionReportService;
import com.service.report.service.MessageReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReportConsumer {
    private final AuctionReportService auctionReportService;
    private final MessageReportService messageReportService;
    private final ConsumerMetrics consumerMetrics;

    @KafkaListener(topics = "reviews.report.auction-approved")
    public void consumeAuctionReportApproved(AuctionReported event) {
        log.info(
                "Evento AuctionReportApproved recebido do Kafka. auctionId={}",
                event.auctionId()
        );
        consumerMetrics.incrementReceived("auction");
        long start = System.currentTimeMillis();
        try {
            auctionReportService.setAuctionReportApproved(event);
            consumerMetrics.incrementProcessed("auction");
            consumerMetrics.recordProcessingTime(
                    "auction",
                    "success",
                    System.currentTimeMillis() - start
            );
            log.debug(
                    "Evento AuctionReportApproved processado com sucesso. auctionId={} correlationId={}",
                    event.auctionId(),
                    event.correlationId()
            );
        } catch (Exception e) {
            consumerMetrics.incrementFailed(
                    "auction",
                    "processing_error"
            );
            consumerMetrics.recordProcessingTime(
                    "auction",
                    "failure",
                    System.currentTimeMillis() - start
            );
            log.error(
                    "Erro ao processar AuctionReportApproved. auctionId={} | erro={}",
                    event.auctionId(),
                    e.getMessage(),
                    e
            );
            throw e;
        }
    }

    @KafkaListener(topics = "reviews.report.qa-approved")
    public void consumeMessageReportApproved(MessageReported event) {
        log.info(
                "Evento MessageReportApproved recebido do Kafka. messageId={}",
                event.messageId()
        );
        consumerMetrics.incrementReceived("message");
        long start = System.currentTimeMillis();
        try {
            messageReportService.setMessageReportApproved(event);
            consumerMetrics.incrementProcessed("message");
            consumerMetrics.recordProcessingTime(
                    "message",
                    "success",
                    System.currentTimeMillis() - start
            );
            log.debug(
                    "Evento MessageReportApproved processado com sucesso. messageId={} correlationId={}",
                    event.messageId(),
                    event.correlationId()
            );
        } catch (Exception e) {
            consumerMetrics.incrementFailed(
                    "message",
                    "processing_error"
            );
            consumerMetrics.recordProcessingTime(
                    "message",
                    "failure",
                    System.currentTimeMillis() - start
            );
            log.error(
                    "Erro ao processar MessageReportApproved. messageId={} | erro={}",
                    event.messageId(),
                    e.getMessage(),
                    e
            );
            throw e;
        }
    }
}