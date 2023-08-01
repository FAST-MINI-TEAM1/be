package fast.mini.be.global.erros.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


// 권한 없음
@Getter
public class Exception403 extends RuntimeException {
    public Exception403(String message) {
        super(message);
    }

    public String body(){
        return getMessage();
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}