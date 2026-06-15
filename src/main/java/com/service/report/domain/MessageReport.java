package com.service.report.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "message_report")
@NoArgsConstructor
@Getter
@Setter
public class MessageReport {
    @Id
    @GeneratedValue(stategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "message_id")
    private Long messageId;

    @Column(nullable = false, name = "seller_id")
    private Long userId;

    @Column(nullable = false, name = "report_reason")
    private String reportReason;

    @Column(nullable = false)
    private boolean reportApproved;

    @Column(nullable = false)
    private Instant creationDate;

    public MessageReport(Long id, Long messageId, Long userId, String reportReason) {
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
        this.reportReason = reportReason;
        this.reportApproved = false;
        this.creationDate = Instant.now();
    }
}
