package at.ac.fhcampuswien.fhmdb.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception{
    private final ExceptionType type;
    private final int errorCode;

    public ApplicationException(ExceptionType type, ErrorCodes errorCode, String message) {
        super(message);
        this.type = type;
        this.errorCode = errorCode.getCode();
    }

}