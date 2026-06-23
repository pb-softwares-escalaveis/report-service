package com.service.report.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ReportMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementCreated(String domain) {
        meterRegistry.counter(
                "reports_created_total",
                "domain", domain
        ).increment();
    }

    public void incrementApproved(String domain) {
        meterRegistry.counter(
                "reports_approved_total",
                "domain", domain
        ).increment();
    }

    public void incrementPersistFailure(String domain) {
        meterRegistry.counter(
                "reports_persist_failed_total",
                "domain", domain
        ).increment();
    }

    public void incrementPublishSuccess(String domain) {
        meterRegistry.counter(
                "reports_events_published_total",
                "domain", domain
        ).increment();
    }

    public void incrementPublishFailure(String domain) {
        meterRegistry.counter(
                "reports_events_publish_failed_total",
                "domain", domain
        ).increment();
    }

    public void incrementNotFound(String domain) {
        meterRegistry.counter(
                "reports_not_found_total",
                "domain", domain
        ).increment();
    }

    public void recordProcessingTime(
            String domain,
            String operation,
            String status,
            long durationMs
    ) {
        Timer.builder("report_processing_seconds")
                .tags(
                        "domain", domain,
                        "operation", operation,
                        "status", status
                )
                .register(meterRegistry)
                .record(durationMs, TimeUnit.MILLISECONDS);
    }
}