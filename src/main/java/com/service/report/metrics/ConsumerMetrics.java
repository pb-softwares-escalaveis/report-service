package com.service.report.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConsumerMetrics {

    private final MeterRegistry meterRegistry;

    public void incrementReceived(String domain) {
        meterRegistry.counter(
                "report_events_received_total",
                "domain", domain
        ).increment();
    }

    public void incrementProcessed(String domain) {
        meterRegistry.counter(
                "report_events_processed_total",
                "domain", domain
        ).increment();
    }

    public void incrementFailed(String domain, String reason) {
        meterRegistry.counter(
                "report_events_failed_total",
                "domain", domain,
                "reason", reason
        ).increment();
    }

    public void recordProcessingTime(String domain, String status, long durationMs) {
        Timer.builder("report_event_processing_seconds")
                .tags(
                        "domain", domain,
                        "status", status
                )
                .register(meterRegistry)
                .record(durationMs, TimeUnit.MILLISECONDS);
    }
}