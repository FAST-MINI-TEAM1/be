package fast.mini.be.domain.user.dto;

import fast.mini.be.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRegisterDto {

    // TODO: 2023/07/30 호윤 - @notblank, @pattern 어노테이션 추가 필요?
    String email;
    String password;
    String empName;
    String rank;


    public User toEntity() {
        return User.builder().email(email).password(password).empName(empName).rank(rank).build();
    }

}
