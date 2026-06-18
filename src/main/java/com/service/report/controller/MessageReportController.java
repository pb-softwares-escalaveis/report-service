package com.service.report.controller;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.exception.InvalidReportRequestException;
import com.service.report.service.MessageReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/report-message")
@RequiredArgsConstructor
public class MessageReportController {

    private final MessageReportService messageReportService;

    @PostMapping
    public ResponseEntity<MessageReport> save(@RequestBody MessageReportRequest request) {
        if (request == null) {
            throw new InvalidReportRequestException("Message report request is required");
        }

        log.info("Recebendo requisição de report de mensagem. messageId={} | auctionId={} | sellerId={} | userId={}",
                request.messageId(), request.auctionId(), request.sellerId(), request.userId());

        MessageReport saved = messageReportService.reportMessage(request);

        log.info("Report de mensagem criado com sucesso. reportId={} | messageId={} | auctionId={}",
                saved.getId(), request.messageId(), request.auctionId());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
