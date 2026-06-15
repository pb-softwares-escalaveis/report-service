package com.service.report.kafka.consumer;

import com.service.report.kafka.events.AuctionReportApproved;
import com.service.report.kafka.events.MessageReportApproved;
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

    @KafkaListener(topics = "reviews.report.auction.approved")
    public void consumeAuctionReportApproved(AuctionReportApproved event) {
        log.info("Evento AuctionReportApproved recebido do Kafka. auctionId={}",
                event.auctionId());

        auctionReportService.setAuctionReportApproved(event);

        log.debug("Evento AuctionReportApproved processado com sucesso. auctionId={}",
                event.auctionId());
    }

    @KafkaListener(topics = "reviews.report.message.approved")
    public void consumeMessageReportApproved(MessageReportApproved event) {
        log.info("Evento MessageReportApproved recebido do Kafka. messageId={}",
                event.messageId());

        messageReportService.setMessageReportApproved(event);

        log.debug("Evento MessageReportApproved processado com sucesso. messageId={}",
                event.messageId());
    }
}