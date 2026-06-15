package com.service.report.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaReportConsumer {
    @KafkaListener(topics = "auctions.lot.created-pending")
    public void consumeAuctionReportApproved() {

    }
}
