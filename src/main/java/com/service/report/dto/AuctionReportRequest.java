package com.service.report.dto;

import java.time.Instant;
import java.util.UUID;

public record AuctionReportRequest(
        UUID userId,
        UUID sellerId,
        Long auctionId,
        String auctionTitle,
        String auctionDescription,
        String reportReason,
        Instant ocurredAt,
        String auctionThumb,
        UUID correlationId
) {}