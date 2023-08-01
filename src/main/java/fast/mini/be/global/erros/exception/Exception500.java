package fast.mini.be.global.erros.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class Exception500 extends RuntimeException {
    public Exception500(String message) {
        super(message);
    }


    public String body() {
        return getMessage();
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
