package fast.mini.be.domain.user.service;

import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserRegisterDto;
import fast.mini.be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void register(UserRegisterDto userRegisterDto) throws Exception {

        User user = userRegisterDto.toEntity();

        user.addUserAuthority();

        user.encodePassword(passwordEncoder);

        /* 예외처리 할때 진행
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()) {

        }

         */
        // TODO: 2023/07/30 호윤 - aes 양방향 암호화 이메일(아이디도 되는지 확인필요) 

        userRepository.save(user);
    }
}
