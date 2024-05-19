package ikharipov.consumerservice.exceptions;

/**
 * Исключение приложения, вызывается при ошибке в работе с метриками.
 */
public class MetricException extends RuntimeApplicationException {
    public MetricException(String message) {
        super(message);
    }

    public MetricException(String message, ErrorCode code) {
        super(message, code);
    }

    public MetricException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricException(String message, ErrorCode code, Throwable cause) {
        super(message, code, cause);
    }
}
