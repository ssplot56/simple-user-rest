package project.module.example.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiException(ErrorCode errorCode, String subText) {
        super(errorCode.getBaseText() + ". " + subText);
        this.httpStatus = errorCode.getHttpStatus();
    }

}
