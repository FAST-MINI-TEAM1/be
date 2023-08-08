package fast.mini.be.domain.user.service;

import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserRegisterDto;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.utils.AES256;
import fast.mini.be.global.utils.exception.BaseException;
import fast.mini.be.global.utils.exception.ExMessage;
import java.util.Calendar;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.source.internal.hbm.XmlElementMetadata;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AES256 aes256;


    @Transactional
    @Override
    public void register(UserRegisterDto userRegisterDto) throws Exception {

        String email = userRegisterDto.getEmail();
        String password = userRegisterDto.getPassword();
        String empName = userRegisterDto.getEmpName();
        String position = userRegisterDto.getPosition();

        if (email == null || password == null || empName == null) {
            throw new BaseException("회원정보가 제대로 입력되지 않았습니다.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new BaseException(ExMessage.USER_ERROR_EMAIL_DUPLICATE);
        } else {
            try {
                User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .empName(aes256.encrypt(empName))
                    .position(position)
                    .role(Role.USER)
                    .annualCount(12)
                    .build();

                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException("회원가입에 실패하였습니다.");
            }
        }
    }
}
