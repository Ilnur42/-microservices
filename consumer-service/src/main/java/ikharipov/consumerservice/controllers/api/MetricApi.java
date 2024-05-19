package ikharipov.consumerservice.controllers.api;

import ikharipov.consumerservice.models.Metric;
import ikharipov.consumerservice.models.MetricType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/metrics")
public interface MetricApi {

    @GetMapping("/between")
    @Operation(
            summary = "Получение метрик между указанными датами",
            description = "Получение коллекции метрик, которые находятся в интервале переданных дат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<List<Metric>> getMetricsCreatedBetween(@RequestParam long firstMinutes, @RequestParam long endMinutes);


    @GetMapping("/after")
    @Operation(
            summary = "Получение метрик, созданных после переданного времени",
            description = "Получение коллекции метрик, которые были созданы после определенной даты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<List<Metric>> getMetricsCreatedAfter(@RequestParam long minutes);


    @GetMapping("/before")
    @Operation(
            summary = "Получение метрик, созданных до переданного времени",
            description = "Получение коллекции метрик, которые были созданы до переданного времени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<List<Metric>> getMetricsCreatedBefore(@RequestParam long minutes);

    @GetMapping("/id")
    @Operation(
            summary = "Получение метрики по переданному идентификатору",
            description = "Получение метрики по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "404", description = "Metric Not Found - Метрика не найдена"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<Metric> getMetricById(@RequestParam UUID metricId);

    @GetMapping("/name")
    @Operation(
            summary = "Получение метрик по наименованию",
            description = "Получение коллекции метрик по переданному наименованию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<List<Metric>> getAllMetricsByName(@RequestParam String name);

    @GetMapping("/type")
    @Operation(
            summary = "Получение метрик определенного типа",
            description = "Получение коллекции метрик по переданному типу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Успешный ответ",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Metric.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Неверный запрос"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Внутренняя ошибка сервера")
    })
    ResponseEntity<List<Metric>> getAllMetricsByType(@RequestParam MetricType type);
}
