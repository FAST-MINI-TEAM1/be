package fast.mini.be.domain.user.controller;

import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserRegisterDto;
import fast.mini.be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody UserRegisterDto userRegisterDto) throws Exception {
        userService.register(userRegisterDto);
    }
}
