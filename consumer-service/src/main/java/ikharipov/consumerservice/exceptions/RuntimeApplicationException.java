package ikharipov.consumerservice.exceptions;

/**
 * Базовый класс для ошибок приложения, опирается на общую базу кодов ошибок.
 */
public class RuntimeApplicationException extends RuntimeException {

    private ErrorCode errorCode;

    public RuntimeApplicationException(String message) {
        super(message);
    }

    public RuntimeApplicationException(String message, ErrorCode code) {
        super(message);
        this.errorCode = code;
    }

    public RuntimeApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeApplicationException(String message, ErrorCode code, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}