package com.service.report.kafka;

import com.service.report.kafka.events.AuctionReported;
import com.service.report.kafka.events.MessageReported;
import com.service.report.kafka.events.ReportEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    @Value("${app.kafka-topics.reports.auction-pending-review}")
    String AUCTION_REPORTED;
    @Value("${app.kafka-topics.reports.qa-pending-review}")
    String MESSAGE_REPORTED;

    private final KafkaTemplate<String, ReportEvent> kafkaTemplate;

    public void sendEvent(ReportEvent event) {
        String kafkaKey = event.auctionId().toString();

        String topic = switch (event) {
            case AuctionReported ignored -> AUCTION_REPORTED;
            case MessageReported ignored -> MESSAGE_REPORTED;
            default -> throw new IllegalArgumentException(
                    "Evento não mapeado para envio: " + event.getClass().getSimpleName());
        };

        log.debug("Publicando evento no Kafka. tipo={} | topic={} | key={}",
                event.getClass().getSimpleName(), topic, kafkaKey);

        kafkaTemplate.send(topic, kafkaKey, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao publicar evento no Kafka. tipo={} | topic={} | key={} | erro={}",
                                event.getClass().getSimpleName(), topic, kafkaKey, ex.getMessage(), ex);
                    } else {
                        log.info("Evento publicado com sucesso no Kafka. tipo={} | topic={} | key={} | partition={} | offset={}",
                                event.getClass().getSimpleName(),
                                topic,
                                kafkaKey,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
