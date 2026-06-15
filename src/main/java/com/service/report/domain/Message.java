package com.service.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Message {
    private UUID userId;
    private Long auctionId;
    private UUID sellerId;
    private Long messageId;
    private String message;
    private String reportReason;
    private Instant ocurredAt;
    private UUID correlationId;

    public Message(Long auctionId, UUID sellerId, Long messageId, String message, Instant ocurredAt, UUID correlationId) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.messageId = messageId;
        this.message = message;
        this.ocurredAt = ocurredAt;
        this.correlationId = correlationId;
    }

    public Message(UUID userId, Long auctionId, UUID sellerId, Long messageId, String message, String reportReason, Instant ocurredAt, UUID correlationId) {
        this.userId = userId;
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.messageId = messageId;
        this.message = message;
        this.reportReason = reportReason;
        this.ocurredAt = ocurredAt;
        this.correlationId = correlationId;
    }
}
