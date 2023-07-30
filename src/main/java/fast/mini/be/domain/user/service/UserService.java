package fast.mini.be.domain.user.service;

import fast.mini.be.domain.user.dto.UserRegisterDto;

public interface UserService {
    void register(UserRegisterDto userRegisterDto) throws Exception;

}
