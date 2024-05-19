package ikharipov.consumerservice.services;

import ikharipov.consumerservice.exceptions.MetricException;
import ikharipov.consumerservice.models.AvailableTag;
import ikharipov.consumerservice.models.Measurement;
import ikharipov.consumerservice.models.Metric;
import ikharipov.consumerservice.models.MetricType;
import ikharipov.consumerservice.repository.AvailableTagRepository;
import ikharipov.consumerservice.repository.MeasurementRepository;
import ikharipov.consumerservice.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с метриками, включает методы для получения и связывания метрик с тегами и измерениями.
 */
@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private AvailableTagRepository availableTagRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    /**
     * Получение метрик, созданных в указанный промежуток времени.
     *
     * @param firstMinutes количество минут с текущего времени для начала периода
     * @param endMinutes количество минут с текущего времени для конца периода
     * @return список метрик, созданных в указанный промежуток времени
     */
    public List<Metric> getMetricsCreatedBetween(long firstMinutes, long endMinutes) {
        Date firstDate = new Date(System.currentTimeMillis() - firstMinutes * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() - endMinutes * 60 * 1000);
        List<Metric> metrics = metricRepository.findMetricsByCreatedBetween(firstDate, endDate);
        return bindAvailableTagsAndMeasurement(metrics);
    }

    /**
     * Получение метрик, созданных после указанного времени.
     *
     * @param minutes количество минут с текущего времени
     * @return список метрик, созданных после указанного времени
     */
    public List<Metric> getMetricsCreatedAfter(long minutes) {
        Date minutesAgo = new Date(System.currentTimeMillis() - minutes * 60 * 1000);
        List<Metric> metrics = metricRepository.findMetricsByCreatedAfter(minutesAgo);
        return bindAvailableTagsAndMeasurement(metrics);
    }

    /**
     * Получение метрик, созданных до указанного времени.
     *
     * @param minutes количество минут с текущего времени
     * @return список метрик, созданных до указанного времени
     */
    public List<Metric> getMetricsCreatedBefore(long minutes) {
        Date minutesAgo = new Date(System.currentTimeMillis() - minutes * 60 * 1000);
        List<Metric> metrics = metricRepository.findMetricsByCreatedBefore(minutesAgo);
        return bindAvailableTagsAndMeasurement(metrics);
    }

    /**
     * Получение метрики по её идентификатору.
     */
    public Metric getMetricById(UUID metricId) {
        Metric metric = metricRepository.findById(metricId).orElseThrow(() -> new MetricException("По переданному идентификатору не найдено метрик"));
        return bindAvailableTagsAndMeasurement(List.of(metric)).get(0);
    }

    /**
     * Получение списка метрик по переданному наименованию метрики.
     */
    public List<Metric> getAllMetricsByName(String name) {
        List<Metric> metrics = metricRepository.findAllByName(name);
        return bindAvailableTagsAndMeasurement(metrics);
    }

    /**
     * Получение метрик по типу.
     */
    public List<Metric> getAllMetricsByType(MetricType type) {
        List<Metric> metrics = metricRepository.findAllByType(type);
        return bindAvailableTagsAndMeasurement(metrics);
    }

    /**
     * Связыванние метрик с их тегами и измерениями.
     *
     * @param metrics список метрик для связывания
     * @return список метрик с установленными тегами и измерениями
     */
    private List<Metric> bindAvailableTagsAndMeasurement(List<Metric> metrics) {
        for (Metric metric : metrics) {
            List<AvailableTag> availableTags = availableTagRepository.findAllByMetric(metric);
            List<Measurement> measurements = measurementRepository.findAllByMetric(metric);
            metric.setMeasurements(measurements);
            metric.setAvailableTags(availableTags);
        }
        return metrics;
    }
}
