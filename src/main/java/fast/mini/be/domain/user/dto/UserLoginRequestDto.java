package fast.mini.be.domain.user.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String email;
    private String password;

}
