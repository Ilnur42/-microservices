package ikharipov.producerservice.jobs;

import ikharipov.producerservice.services.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Класс отвечает за периодическую отправку метрик в Kafka.
 */
@Component
public class MetricsRunner {

    @Autowired
    private KafkaService kafkaService;

    /**
     * Периодическая отправка JVM метрик по расписанию, заданному в пропертях.
     */
    @Scheduled(cron = "${cron.metric.sending.jvm}")
    public void runJvmMetricsSending() {
        kafkaService.sendJvmMetric();
    }

    /**
     * Периодическая отправка процессорных метрик по расписанию, заданному в пропертях.
     */
    @Scheduled(cron = "${cron.metric.sending.process}")
    public void runProcessMetricsSending() {
        kafkaService.sendProcessMetric();
    }

    /**
     * Периодическая отправка HTTP метрик по расписанию, заданному в пропертях.
     */
    @Scheduled(cron = "${cron.metric.sending.http}")
    public void runHttpMetricsSending() {
        kafkaService.sendHttpMetric();
    }

    /**
     * Периодическая отправка всех трех типов метрик по расписанию, заданному в пропертях.
     */
    @Scheduled(cron = "${cron.metric.sending.all}")
    public void runAllMetricsSending() {
        kafkaService.sendJvmMetric();
        kafkaService.sendProcessMetric();
        kafkaService.sendHttpMetric();
    }
}
