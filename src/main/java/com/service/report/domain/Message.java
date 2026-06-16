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
    private Instant occurredAt;
    private UUID correlationId;

    public Message(Long auctionId, UUID sellerId, Long messageId, String message, Instant occurredAt, UUID correlationId) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.messageId = messageId;
        this.message = message;
        this.occurredAt = occurredAt;
        this.correlationId = correlationId;
    }

    public Message(UUID userId, Long auctionId, UUID sellerId, Long messageId, String message, String reportReason, Instant occurredAt, UUID correlationId) {
        this.userId = userId;
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.messageId = messageId;
        this.message = message;
        this.reportReason = reportReason;
        this.occurredAt = occurredAt;
        this.correlationId = correlationId;
    }
}
