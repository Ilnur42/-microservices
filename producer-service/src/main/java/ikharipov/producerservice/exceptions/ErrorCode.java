package ikharipov.producerservice.exceptions;

import java.util.Arrays;

/**
 * База кодов ошибок
 */
public enum ErrorCode {

    DATA_ACCESS_ERROR(0x0001, "Ошибка доступа к данным"),
    INTERNAL_SERVER_ERROR(0x0002, "Внутренняя ошибка сервера"),
    BAD_ARGUMENTS(0x0003, "Неверные параметры"),
    ENTITY_NOT_FOUND(0x0004, "Сущность не найдена");

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCode getByCode(int code) {
        return Arrays.stream(values()).filter(c -> c.getCode() == code).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

    public String getCodeAsString() {
        return String.valueOf(code);
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + this.name() + ": " + msg;
    }
}