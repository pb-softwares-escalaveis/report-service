package com.service.report.kafka.events;

import java.time.Instant;
import java.util.UUID;

public interface ReportEvent {
    Long auctionId();
    UUID sellerId();
    Instant occurredAt();
    UUID correlationId();
}
