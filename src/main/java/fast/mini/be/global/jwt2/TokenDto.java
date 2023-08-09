package fast.mini.be.global.jwt2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

}
