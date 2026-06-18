package com.service.report.dto;

import java.util.UUID;

public record AuctionReportRequest(
        UUID userId,
        UUID sellerId,
        Long auctionId,
        String auctionTitle,
        String auctionDescription,
        String reportReason,
        String auctionThumb
) {}
