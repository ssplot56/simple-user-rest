package project.module.example.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR("Not correct value"),
    ALREADY_REGISTERED("User is already registered"),
    NO_ENTITY("Entity not found");

    ErrorCode(String baseText) {
        this(baseText, HttpStatus.BAD_REQUEST);
    }

    private final String baseText;
    private final HttpStatus httpStatus;
}
