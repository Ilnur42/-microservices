package ikharipov.producerservice.exceptions;

/**
 * Исключение приложения, вызывается при ошибке в работе с кафкой.
 */
public class KafkaException extends RuntimeApplicationException {

    public KafkaException(String message) {
        super(message);
    }

    public KafkaException(String message, ErrorCode code) {
        super(message, code);
    }

    public KafkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaException(String message, ErrorCode code, Throwable cause) {
        super(message, code, cause);
    }
}
