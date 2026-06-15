package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record AuctionReportedPendingReview(
        UUID userId,
        Long auctionId,
        UUID sellerId,
        String auctionTitle,
        String auctionDescription,
        Instant ocurredAt,
        String auctionThumb,
        UUID correlationId
) implements ReportEvent {
}
