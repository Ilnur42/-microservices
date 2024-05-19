package ikharipov.producerservice.controllers;

import ikharipov.producerservice.jobs.MetricsRunner;
import ikharipov.producerservice.services.KafkaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/metrics")
public class MetricController {

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private MetricsRunner metricsRunner;

    @Operation(summary = "Запуск периодической отправки всех метрик")
    @PostMapping(path = "start/all")
    public void startSendAllMetrics() {
        metricsRunner.runAllMetricsSending();
    }

    @Operation(summary = "Запуск периодической отправки JVM метрик")
    @PostMapping(path = "start/jvm")
    public void startSendJvmMetrics() {
        metricsRunner.runJvmMetricsSending();
    }

    @Operation(summary = "Запуск периодической отправки процессорных метрик")
    @PostMapping(path = "start/process")
    public void startSendProcessMetrics() {
        metricsRunner.runProcessMetricsSending();
    }

    @Operation(summary = "Запуск периодической отправки http метрик")
    @PostMapping(path = "start/http")
    public void startSendHttpMetrics() {
        metricsRunner.runHttpMetricsSending();
    }

    @Operation(summary = "Отправка всех метрик")
    @PostMapping(path = "send/all")
    public void sendAllMetrics() {
        kafkaService.sendJvmMetric();
        kafkaService.sendProcessMetric();
        kafkaService.sendHttpMetric();
    }

    @Operation(summary = "Отправкка JVM метрик")
    @PostMapping(path = "send/jvm")
    public void sendJvmMetrics() {
        kafkaService.sendJvmMetric();
    }

    @Operation(summary = "Отправка процессорных метрик")
    @PostMapping(path = "send/process")
    public void sendProcessMetrics() {
        kafkaService.sendProcessMetric();
    }

    @Operation(summary = "Отправка http метрик")
    @PostMapping(path = "send/http")
    public void sendHttpMetrics() {
        kafkaService.sendHttpMetric();
    }
}
