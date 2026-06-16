package com.service.report.service;

import com.service.report.domain.AuctionReport;
import com.service.report.dto.AuctionReportRequest;
import com.service.report.kafka.KafkaService;
import com.service.report.kafka.events.AuctionReported;
import com.service.report.repository.AuctionReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionReportService {

    private final AuctionReportRepository auctionReportRepository;
    private final KafkaService kafkaService;

    @Transactional
    public AuctionReport reportAuction(AuctionReportRequest request) {
        log.info("Iniciando report de leilão. auctionId={} | sellerId={} | userId={}",
                request.auctionId(), request.sellerId(), request.userId());

        AuctionReport report = new AuctionReport(
                request.auctionId(),
                request.sellerId(),
                request.reportReason()
        );

        AuctionReport saved = auctionReportRepository.save(report);
        log.info("Report de leilão persistido com sucesso. reportId={} | auctionId={} | sellerId={}",
                saved.getId(), request.auctionId(), request.sellerId());

        AuctionReported event = new AuctionReported(
                request.userId(),
                request.auctionId(),
                request.sellerId(),
                request.auctionTitle(),
                request.auctionDescription(),
                request.reportReason(),
                request.occurredAt(),
                request.auctionThumb(),
                request.correlationId()
        );

        log.debug("Publicando evento AuctionReportedPendingReview no Kafka. auctionId={} | correlationId={}",
                request.auctionId(), request.correlationId());

        kafkaService.sendEvent(event);

        log.info("Report de leilão concluído. reportId={} | auctionId={} | sellerId={}",
                saved.getId(), request.auctionId(), request.sellerId());

        return saved;
    }

    public AuctionReport setAuctionReportApproved(AuctionReported AuctionReported) {
        log.info("Processando aprovação de report de leilão. auctionId={}",
                AuctionReported.auctionId());

        AuctionReport report = auctionReportRepository.findById(AuctionReported.auctionId())
                .orElseThrow(() -> {
                    log.error("AuctionReport não encontrado. auctionId={}", AuctionReported.auctionId());
                    return new EntityNotFoundException(
                            "AuctionReport not found for auctionId: " + AuctionReported.auctionId()
                    );
                });

        report.setReportApproved(true);

        AuctionReport saved = auctionReportRepository.save(report);
        log.info("Report de leilão marcado como aprovado com sucesso. reportId={} | auctionId={}",
                saved.getId(), AuctionReported.auctionId());

        return saved;
    }
}