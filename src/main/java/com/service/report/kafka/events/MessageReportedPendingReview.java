package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReportedPendingReview(
        UUID userId,
        Long auctionId,
        UUID sellerId,
        Long messageId,
        String message,
        Instant ocurredAt,
        UUID correlationId
) implements ReportEvent {
}
