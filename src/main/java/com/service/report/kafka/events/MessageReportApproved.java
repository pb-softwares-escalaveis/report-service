package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReportApproved(
        Long auctionId,
        UUID sellerId,
        Long messageId,
        Instant occurredAt,
        UUID correlationId
) implements ReportEvent {
}
