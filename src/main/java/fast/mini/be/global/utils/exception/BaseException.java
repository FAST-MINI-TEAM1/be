package fast.mini.be.global.utils.exception;

import org.apache.logging.log4j.message.ExitMessage;
import org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyProperties;

public class BaseException extends RuntimeException {

    public BaseException(ExMessage exMessage) {
        super(exMessage.getMessage());
    }

    public BaseException(String message) {
        super(message);
    }

}
