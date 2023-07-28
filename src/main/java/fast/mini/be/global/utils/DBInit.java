package fast.mini.be.global.utils;

import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class DBInit {
    private final AES256 AES256;
    private final PasswordEncoder passwordEncoder; //BcryptPasswordEncoder
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initDB(){
        return args -> {
            User user1 = User.builder()
                    .username("lphilcock3@latimes.com")
                    .password(passwordEncoder.encode("lphilcock3!"))
                    .empName(AES256.encrypt("박지훈"))
                    .empNo("20200001")
                    .rank("팀장")
                    .role(Role.USER)
                    .annualCount(3)
                    .build();
            user1.setCreatedAt(LocalDateTime.of(2020,11,29,11,27,15));
            userRepository.save(user1);

            User user2 = User.builder()
                    .username("ebunce1@bravesites.com")
                    .password(passwordEncoder.encode("ebunce1!"))
                    .empName(AES256.encrypt("김철수"))
                    .empNo("20200002")
                    .role(Role.USER)
                    .annualCount(10)
                    .build();
            user2.setCreatedAt(LocalDateTime.of(2020,12,12,12,16,48));
            userRepository.save(user2);

            User user3 = User.builder()
                    .username("asmallpeice8@myspace.com")
                    .password(passwordEncoder.encode("asmallpeice8!"))
                    .empName(AES256.encrypt("박상훈"))
                    .empNo("20200003")
                    .rank("차장")
                    .role(Role.USER)
                    .annualCount(7)
                    .build();
            user3.setCreatedAt(LocalDateTime.of(2020,12,21,19,8,53));
            userRepository.save(user3);

            User user4 = User.builder()
                    .username("oleming0@typepad.com")
                    .password(passwordEncoder.encode("oleming0!"))
                    .empName(AES256.encrypt("홍길동"))
                    .empNo("20210004")
                    .rank("과장")
                    .role(Role.USER)
                    .annualCount(3)
                    .build();
            user4.setCreatedAt(LocalDateTime.of(2021,4,28,10,25,0));
            userRepository.save(user4);

            User user5 = User.builder()
                    .username("bbedding6@themeforest.net")
                    .password(passwordEncoder.encode("bbedding6!"))
                    .empName(AES256.encrypt("윤서연"))
                    .empNo("20210005")
                    .rank("대리")
                    .role(Role.USER)
                    .annualCount(2)
                    .build();
            user5.setCreatedAt(LocalDateTime.of(2021,4,29,10,48,31));
            userRepository.save(user5);

            User user6 = User.builder()
                    .username("oshipcott4@usgs.gov")
                    .password(passwordEncoder.encode("oshipcott4!"))
                    .empName(AES256.encrypt("이지영"))
                    .empNo("20210006")
                    .role(Role.USER)
                    .annualCount(6)
                    .build();
            user6.setCreatedAt(LocalDateTime.of(2021,6,8,21,33,51));
            userRepository.save(user6);

            User user7 = User.builder()
                    .username("taron2@wufoo.com")
                    .password(passwordEncoder.encode("taron2!"))
                    .empName(AES256.encrypt("김영희"))
                    .empNo("20210007")
                    .role(Role.USER)
                    .annualCount(3)
                    .build();
            user7.setCreatedAt(LocalDateTime.of(2021,9,6,23,32,54));
            userRepository.save(user7);

            User user8 = User.builder()
                    .username("wdemaine9@example.com")
                    .password(passwordEncoder.encode("wdemaine9!"))
                    .empName(AES256.encrypt("김동현"))
                    .empNo("20210008")
                    .rank("대리")
                    .role(Role.USER)
                    .annualCount(12)
                    .build();
            user8.setCreatedAt(LocalDateTime.of(2021,9,29,17,54,3));
            userRepository.save(user8);

            User user9 = User.builder()
                    .username("mkellet5@canalblog.com")
                    .password(passwordEncoder.encode("mkellet5!"))
                    .empName(AES256.encrypt("최민준"))
                    .empNo("20210009")
                    .role(Role.USER)
                    .annualCount(7)
                    .build();
            user9.setCreatedAt(LocalDateTime.of(2021,10,14,6,7,17));
            userRepository.save(user9);

            User user10 = User.builder()
                    .username("hoflaherty7@cbslocal.com")
                    .password(passwordEncoder.encode("hoflaherty7!"))
                    .empName(AES256.encrypt("이예준"))
                    .empNo("20210010")
                    .rank("사원")
                    .role(Role.USER)
                    .annualCount(12)
                    .build();
            user10.setCreatedAt(LocalDateTime.of(2021,10,29,10,18,32));
            userRepository.save(user10);
        };
    }
}
