package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record MessageReportApproved(
        Long auctionId,
        UUID sellerId,
        String repprovedReason,
        Long messageId,
        Instant ocurredAt,
        UUID correlationId
) implements ReportEvent {
}
