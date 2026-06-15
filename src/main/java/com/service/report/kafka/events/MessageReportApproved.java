package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReportApproved(
        Long auctionId,
        UUID sellerId,
        Long messageId,
        Instant ocurredAt,
        UUID correlationId
) implements ReportEvent {
}
