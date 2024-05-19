package ikharipov.consumerservice.controllers;

import ikharipov.consumerservice.controllers.api.MetricApi;
import ikharipov.consumerservice.models.Metric;
import ikharipov.consumerservice.models.MetricType;
import ikharipov.consumerservice.services.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class MetricController implements MetricApi {

    private static final Logger logger = LoggerFactory.getLogger(MetricController.class);

    @Autowired
    private MetricService metricService;

    @Override
    public ResponseEntity<List<Metric>> getMetricsCreatedBetween(long firstMinutes, long endMinutes) {
        try {
            List<Metric> metrics = metricService.getMetricsCreatedBetween(firstMinutes, endMinutes);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Ошибка при получении метрик между инитервалом  {} и {} минут: {}", firstMinutes, endMinutes, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Metric>> getMetricsCreatedAfter(long minutes) {
        try {
            List<Metric> metrics = metricService.getMetricsCreatedAfter(minutes);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Ошибка при получении метрик после {} минут: {}", minutes, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Metric>> getMetricsCreatedBefore(long minutes) {
        try {
            List<Metric> metrics = metricService.getMetricsCreatedBefore(minutes);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Ошибка при получении метрик до {} минуты: {}", minutes, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Metric> getMetricById(UUID metricId) {
        try {
            Metric metric = metricService.getMetricById(metricId);
            if (metric != null) {
                return ResponseEntity.ok(metric);
            } else {
                logger.warn("Метрика с идентификатором {} не найдена", metricId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении метрики по идентификатору {}: {}", metricId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Metric>> getAllMetricsByName(String name) {
        try {
            List<Metric> metrics = metricService.getAllMetricsByName(name);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Ошибка при получении метрик по наименованию {}: {}", name, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Metric>> getAllMetricsByType(MetricType type) {
        try {
            List<Metric> metrics = metricService.getAllMetricsByType(type);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Ошибка при получении метрик по типу {}: {}", type, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
