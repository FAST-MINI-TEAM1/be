package fast.mini.be.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginResponseDto {

    private Long id;
    private String email;
    private String empName;
    private String empNo;
    private String position;
    private String accessToken;
    private String refreshToken;

}
