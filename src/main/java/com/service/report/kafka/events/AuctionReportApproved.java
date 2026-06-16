package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record AuctionReportApproved(
        Long auctionId,
        UUID sellerId,
        Instant occurredAt,
        UUID correlationId
) implements ReportEvent {
}
