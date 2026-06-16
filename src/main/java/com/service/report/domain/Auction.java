package com.service.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Auction {
    private UUID userId;
    private Long auctionId;
    private UUID sellerId;
    private String auctionTitle;
    private String auctionDescription;
    private String reportReason;
    private Instant occurredAt;
    private String auctionThumb;
    private UUID correlationId;

    public Auction(Long auctionId, UUID sellerId, String auctionTitle, String auctionDescription, Instant occurredAt, String auctionThumb, UUID correlationId) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.auctionTitle = auctionTitle;
        this.auctionDescription = auctionDescription;
        this.occurredAt = occurredAt;
        this.auctionThumb = auctionThumb;
        this.correlationId = correlationId;
    }

    public Auction(UUID userId, Long auctionId, UUID sellerId, String auctionTitle, String auctionDescription, String reportReason, Instant occurredAt, String auctionThumb, UUID correlationId) {
        this.userId = userId;
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.auctionTitle = auctionTitle;
        this.auctionDescription = auctionDescription;
        this.reportReason = reportReason;
        this.occurredAt = occurredAt;
        this.auctionThumb = auctionThumb;
        this.correlationId = correlationId;
    }
}