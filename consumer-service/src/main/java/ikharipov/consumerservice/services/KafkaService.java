package ikharipov.consumerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ikharipov.consumerservice.models.Metric;
import ikharipov.consumerservice.repository.MetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Сервис для получения и обработки сообщений из Kafka, содержащих метрики.Обрабатывает сообщения из различных топиков,
 * сохраняет метрики в базу данных, а также обрабатывает ошибки, отправляя сообщения в DLT.
 */
@Service
public class KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Value("${metrics.jvm.topic.name}")
    private String jvmMetricsTopic;
    @Value("${metrics.process.topic.name}")
    private String processMetricsTopic;
    @Value("${metrics.http.topic.name}")
    private String httpMetricsTopic;
    @Value("${metrics.jvm.topic.name.dlt}")
    private String jvmMetricsTopicDlt;
    @Value("${metrics.process.topic.name.dlt}")
    private String processMetricsTopicDlt;
    @Value("${metrics.http.topic.name.dlt}")
    private String httpMetricsTopicDlt;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplates;
    @Autowired
    private MetricRepository metricRepository;

    /**
     * Получение сообщений с метриками JVM из Kafka, их парсинг их и сохранение в репозиторий.
     */
    @KafkaListener(id = "jvmMetricsGroup", topics = "${metrics.jvm.topic.name}")
    @RetryableTopic(attempts = "5", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
    public void consumeJvmMetricMessage(String message) {
        try {
            List<Metric> jvmMetrics = objectMapper.readValue(message, new TypeReference<List<Metric>>() {});
            saveLinks(jvmMetrics);
            metricRepository.saveAll(jvmMetrics);
            logger.info("Принято и сохранено {} jvm метрик", jvmMetrics.size());
        } catch (JsonProcessingException ex) {
            handleProcessingError(ex, message, jvmMetricsTopicDlt);
        }
    }

    /**
     * Получение сообщений с процессорными метриками из Kafka, их парсинг их и сохранение в репозиторий.
     */
    @KafkaListener(id = "processMetricsGroup", topics = "${metrics.process.topic.name}")
    @RetryableTopic(attempts = "5", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
    public void consumeProcessMetricMessage(String message) {
        try {
            List<Metric> processMetrics = objectMapper.readValue(message, new TypeReference<List<Metric>>() {});
            saveLinks(processMetrics);
            metricRepository.saveAll(processMetrics);
            logger.info("Принято и сохранено процессорных метрик {}", processMetrics.size());
        } catch (JsonProcessingException ex) {
            handleProcessingError(ex, message, processMetricsTopicDlt);
        }
    }

    /**
     * Получение сообщений с HTTP метриками из Kafka, их парсинг их и сохранение в репозиторий.
     */
    @KafkaListener(id = "httpMetricsGroup", topics = "${metrics.http.topic.name}")
    @RetryableTopic(attempts = "5", backoff = @Backoff(delay = 2000, maxDelay = 10000, multiplier = 2))
    public void consumeHttpMetricMessage(String message) {
        try {
            List<Metric> httpMetrics = objectMapper.readValue(message, new TypeReference<List<Metric>>() {});
            saveLinks(httpMetrics);
            metricRepository.saveAll(httpMetrics);
            logger.info("Принято и сохранено {} http метрик", httpMetrics.size());
        } catch (JsonProcessingException ex) {
            handleProcessingError(ex, message, httpMetricsTopicDlt);
        }
    }

    /**
     * Обработка исключений при обработке сообщений, отправляя сообщение в DLT.
     */
    private void handleProcessingError(Exception ex, String message, String dltTopic) {
        logger.info("Отправка в DLT (Dead Letter Topic): {}", message);
        try {
            kafkaTemplates.send(dltTopic, message);
        } catch (KafkaException e) {
            throw new KafkaException(MessageFormat.format("Ошибка обработки сообщения: {}", ex.getMessage()));
        }
    }

    /**
     * Получение сообщений из JVM DLT топика и их логирование.
     */
    @KafkaListener(id = "jvmDltGroup", topics = "${metrics.jvm.topic.name.dlt}")
    public void dltListenJvm(byte[] in) {
        logger.info("Получено из DLT (Jvm): {}", new String(in));
    }

    /**
     * Получение сообщений из процессорных DLT топика и их логирование.
     */
    @KafkaListener(id = "processDltGroup", topics = "${metrics.process.topic.name.dlt}")
    public void dltListenProcess(byte[] in) {
        logger.info("Получено из DLT (Процессор): {}", new String(in));
    }

    /**
     * Получение сообщений из HTTP DLT топика и их логирование.
     */
    @KafkaListener(id = "httpDltGroup", topics = "${metrics.http.topic.name.dlt}")
    public void dltListenHttp(byte[] in) {
        logger.info("Получено из DLT (HTTP): {}", new String(in));
    }

    /**
     * Устанавливает обратные ссылки для метрик и их связанных тегов и измерений.
     */
    private void saveLinks(List<Metric> metrics) {
        for (Metric metric : metrics) {
            if (metric.getAvailableTags() != null && !metric.getAvailableTags().isEmpty()) {
                metric.getAvailableTags().forEach(e -> e.setMetric(metric));
            }
            if (metric.getMeasurements() != null && !metric.getMeasurements().isEmpty()) {
                metric.getMeasurements().forEach(e -> e.setMetric(metric));
            }
        }
    }
}
