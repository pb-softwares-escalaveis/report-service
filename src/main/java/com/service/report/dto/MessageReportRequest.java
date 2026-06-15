package com.service.report.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageReportRequest(
        UUID userId,
        Long auctionId,
        UUID sellerId,
        Long messageId,
        String message,
        String reportReason,
        Instant ocurredAt,
        UUID correlationId
) {
}