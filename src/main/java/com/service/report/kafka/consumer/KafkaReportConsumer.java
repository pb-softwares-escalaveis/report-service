package com.service.report.kafka.consumer;

import com.service.report.kafka.events.AuctionReportApproved;
import com.service.report.kafka.events.MessageReportApproved;
import com.service.report.service.AuctionReportService;
import com.service.report.service.MessageReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaReportConsumer {
    private final AuctionReportService auctionReportService;
    private final MessageReportService messageReportService;

    @KafkaListener(topics = "reviews.report.auction.approved")
    public void consumeAuctionReportApproved(AuctionReportApproved event) {
        auctionReportService.setAuctionReportApproved(event);
    }

    @KafkaListener(topics = "reviews.report.message.approved")
    public void consumeMessageReportApproved(MessageReportApproved event) {
        messageReportService.setMessageReportApproved(event);
    }
}
