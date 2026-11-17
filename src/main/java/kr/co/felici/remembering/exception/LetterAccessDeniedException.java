package kr.co.felici.remembering.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class LetterAccessDeniedException extends RuntimeException {

    public LetterAccessDeniedException(Long letterId) {
        super("편지에 대한 권한이 없습니다. id=" + letterId);
    }
}
