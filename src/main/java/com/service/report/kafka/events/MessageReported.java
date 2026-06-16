package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReported(
        UUID userId,
        UUID sellerId,
        Long auctionId,
        Long messageId,
        String message,
        String reportReason,
        Instant occurredAt,
        UUID correlationId
) implements ReportEvent {
}
