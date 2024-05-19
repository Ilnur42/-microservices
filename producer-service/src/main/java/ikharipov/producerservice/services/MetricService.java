package ikharipov.producerservice.services;

import ikharipov.producerservice.models.Metric;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Сервисный класс предназначенный для взаимодействия с внешним сервисом метрик.
 */
@Service
public class MetricService {

    private final WebClient webClient;

    public MetricService(WebClient.Builder webClientBuilder, @Value("${metrics.service.url}") String metricsServiceUr) {
        this.webClient = webClientBuilder.baseUrl(metricsServiceUr).build();
    }

    /**
     * Получение метрики по имени, запрашивая её из внешнего сервиса метрик.
     */
    public Mono<Metric> getMetricByName(String metricName) {
        return webClient.get()
                .uri("/metrics/{name}", metricName)
                .retrieve()
                .bodyToMono(Metric.class);
    }
}
