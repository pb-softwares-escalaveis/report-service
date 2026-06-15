package com.service.report.controller;

import com.service.report.domain.MessageReport;
import com.service.report.dto.MessageReportRequest;
import com.service.report.service.MessageReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report-message")
@RequiredArgsConstructor
public class MessageReportController {

    private final MessageReportService messageReportService;

    @PostMapping
    public ResponseEntity<MessageReport> save(@RequestBody MessageReportRequest request) {
        MessageReport saved = messageReportService.reportMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}