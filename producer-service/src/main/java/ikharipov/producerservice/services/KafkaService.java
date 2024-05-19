package ikharipov.producerservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ikharipov.producerservice.models.Metric;
import ikharipov.producerservice.models.MetricType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Сервис предназначен для отправки метрик в Kafka.
 */
@Service
public class KafkaService implements AutoCloseable {

    @Value("${metrics.jvm.topic.name}")
    private String jvmMetricsTopic;
    @Value("${metrics.process.topic.name}")
    private String processMetricsTopic;
    @Value("${metrics.http.topic.name}")
    private String httpMetricsTopic;

    @Autowired
    private MetricService metricService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Отправление JVM метрик с соответствующим топиком в Kafka.
     */
    public void sendJvmMetric() {
        Flux<Metric> jvmMetrics = Flux.concat(
                metricService.getMetricByName("jvm.memory.used"),
                metricService.getMetricByName("jvm.memory.max"),
                metricService.getMetricByName("jvm.threads.live")
        ).map(metric -> {
            metric.setType(MetricType.JVM);
            return metric;
        });
        sendMetrics(jvmMetrics, jvmMetricsTopic);
    }

    /**
     * Отправление процессорных метрик с соответствующим топиком в Kafka.
     */
    public void sendProcessMetric() {
        Flux<Metric> processMetrics = Flux.concat(
                metricService.getMetricByName("process.cpu.usage"),
                metricService.getMetricByName("process.uptime")
        ).map(metric -> {
            metric.setType(MetricType.Process);
            return metric;
        });
        sendMetrics(processMetrics, processMetricsTopic);
    }

    /**
     * Отправление HTTP метрик с соответствующим топиком в Kafka.
     */
    public void sendHttpMetric() {
        Flux<Metric> httpMetrics = metricService.getMetricByName("http.server.requests").flux().map(metric -> {
            metric.setType(MetricType.HTTP);
            return metric;
        });
        sendMetrics(httpMetrics, httpMetricsTopic);
    }

    /**
     * Непосредственная отправка заданных метрик в указанную тему Kafka.
     */
    private void sendMetrics(Flux<Metric> metrics, String topicName) {
        metrics.collectList().subscribe(
                list -> {
                    try {
                        String jsonString = objectMapper.writeValueAsString(list);
                        kafkaTemplate.send(topicName, jsonString);
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка сериализации метрики в JSON", e);
                    }
                },
                error -> {
                    throw new RuntimeException("Error collecting metrics list", error);
                }
        );
    }

    /**
     * Закрытие KafkaTemplate при завершении работы.
     */
    @Override
    public void close() {
        kafkaTemplate.destroy();
    }
}
