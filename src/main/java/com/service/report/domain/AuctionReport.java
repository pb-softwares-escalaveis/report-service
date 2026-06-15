package com.service.report.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auction_report")
@NoArgsConstructor
@Getter
@Setter
public class AuctionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "auction_id")
    private Long auctionId;

    @Column(nullable = false, name = "seller_id")
    private UUID sellerId;

    @Column(nullable = false, name = "report_reason")
    private String reportReason;

    @Column(nullable = false)
    private boolean reportApproved;

    @Column(nullable = false)
    private Instant creationDate;

    public AuctionReport(Long auctionId, UUID sellerId, String reportReason) {
        this.auctionId = auctionId;
        this.sellerId = sellerId;
        this.reportReason = reportReason;
        this.reportApproved = false;
        this.creationDate = Instant.now();
    }
}