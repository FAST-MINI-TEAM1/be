package fast.mini.be.global.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExMessage {
    USER_ERROR_DB_SAVE("회원을 DB에 저장하는 중에 문제가 발생했습니다."),
    USER_ERROR_EMAIL_DUPLICATE("이메일이 중복되었습니다.");

    private final String message;
}
