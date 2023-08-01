package fast.mini.be.global.erros.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public String body(){
        return getMessage();
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}