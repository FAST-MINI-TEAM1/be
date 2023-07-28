package fast.mini.be.global.utils;

import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class DBInit {
    private final DataSource dataSource;
    private final AES256 AES256;
    private final PasswordEncoder passwordEncoder; //BcryptPasswordEncoder
    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initDB(){
        return args -> {
            initUser();
            initOrder();
            initApproveDate();
        };
    }

    private void initUser() throws Exception {
        User user1 = User.builder()
                .username("lphilcock3@latimes.com")
                .password(passwordEncoder.encode("lphilcock3!"))
                .empName(AES256.encrypt("박지훈"))
                .empNo("20200001")
                .position("팀장")
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
                .position("차장")
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
                .position("과장")
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
                .position("대리")
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
                .position("대리")
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
                .position("사원")
                .role(Role.USER)
                .annualCount(12)
                .build();
        user10.setCreatedAt(LocalDateTime.of(2021,10,29,10,18,32));
        userRepository.save(user10);
    }

    private void initOrder() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "INSERT INTO order_tb (user_id, order_type, start_date, end_date, status, reason, category, etc, created_at) VALUES " +
                    "(1, 'ANNUAL', '2023-07-15', '2023-07-20', 'WAIT', '이유', '경조사', '기타', '2023-07-15 09:00:00'),"+
                    "(2, 'DUTY', '2023-08-10', '2023-08-10', 'APPROVE', NULL, NULL, NULL, '2023-08-10 10:15:00'),"+
                    "(3, 'ANNUAL', '2023-08-25', '2023-08-27', 'REJECT', '이유', '병가', '기타', '2023-08-25 11:30:00'),"+
                    "(4, 'ANNUAL', '2023-09-05', '2023-09-10', 'APPROVE', '이유', '생리휴가', '기타', '2023-09-05 12:45:00'),"+
                    "(5, 'DUTY', '2023-09-20', '2023-09-20', 'WAIT', NULL, NULL, NULL, '2023-09-18 14:00:00'),"+
                    "(6, 'ANNUAL', '2023-10-05', '2023-10-06', 'APPROVE', '이유', '출산휴가', '기타', '2023-09-28 15:15:00'),"+
                    "(7, 'ANNUAL', '2023-10-20', '2023-10-25', 'REJECT', '이유', '경조사', '기타', '2023-10-18 16:30:00'),"+
                    "(8, 'ANNUAL', '2023-11-05', '2023-11-10', 'WAIT', '이유', '병가', '기타', '2023-10-28 17:45:00'),"+
                    "(9, 'DUTY', '2023-11-20', '2023-11-20', 'WAIT', NULL, NULL, NULL, '2023-11-18 18:00:00'),"+
                    "(10, 'ANNUAL', '2023-12-05', '2023-12-06', 'WAIT', '이유', '생리휴가', '기타', '2023-12-05 19:15:00'),"+
                    "(1, 'DUTY', '2023-12-20', '2023-12-20', 'APPROVE', NULL, NULL, NULL, '2023-12-20 20:30:00'),"+
                    "(2, 'ANNUAL', '2023-01-05', '2023-01-10', 'APPROVE', '이유', '출산휴가', '기타', '2023-01-05 21:45:00'),"+
                    "(3, 'ANNUAL', '2023-01-20', '2023-01-25', 'WAIT', '이유', '경조사', '기타', '2023-01-20 22:00:00'),"+
                    "(4, 'ANNUAL', '2023-02-05', '2023-02-10', 'REJECT', '이유', '병가', '기타', '2023-02-05 23:15:00'),"+
                    "(5, 'DUTY', '2023-02-20', '2023-02-20', 'APPROVE', NULL, NULL, NULL, '2023-02-20 00:30:00'),"+
                    "(6, 'ANNUAL', '2023-02-20', '2023-03-10', 'WAIT', '이유', '생리휴가', '기타', '2023-02-20 01:45:00'),"+
                    "(7, 'DUTY', '2023-03-20', '2023-03-20', 'APPROVE', NULL, NULL, NULL, '2023-03-20 02:00:00'),"+
                    "(8, 'ANNUAL', '2023-04-05', '2023-04-10', 'WAIT', '이유', '출산휴가', '기타', '2023-04-05 03:15:00'),"+
                    "(9, 'ANNUAL', '2023-04-20', '2023-04-25', 'WAIT', '이유', '경조사', '기타', '2023-04-20 04:30:00'),"+
                    "(10, 'ANNUAL', '2023-05-05', '2023-05-10', 'APPROVE', '이유', '병가', '기타', '2023-04-29 05:45:00'),"+
                    "(1, 'DUTY', '2023-11-20', '2023-11-20', 'APPROVE', NULL, NULL, NULL, '2023-11-20 20:30:00'),"+
                    "(2, 'ANNUAL', '2023-05-05', '2023-05-09', 'APPROVE', '이유', '출산휴가', '기타', '2023-05-05 21:45:00'),"+
                    "(3, 'ANNUAL', '2023-02-20', '2023-02-25', 'WAIT', '이유', '경조사', '기타', '2023-02-20 22:00:00'),"+
                    "(4, 'ANNUAL', '2023-03-05', '2023-03-10', 'REJECT', '이유', '병가', '기타', '2023-03-05 23:15:00'),"+
                    "(5, 'DUTY', '2023-07-20', '2023-07-20', 'APPROVE', NULL, NULL, NULL, '2023-07-18 00:30:00'),"+
                    "(6, 'ANNUAL', '2023-05-05', '2023-05-10', 'WAIT', '이유', '생리휴가', '기타', '2023-04-29 01:45:00'),"+
                    "(7, 'DUTY', '2023-01-20', '2023-01-20', 'APPROVE', NULL, NULL, NULL, '2023-01-20 02:00:00'),"+
                    "(8, 'ANNUAL', '2023-05-08', '2023-05-10', 'WAIT', '이유', '출산휴가', '기타', '2023-05-05 03:15:00'),"+
                    "(9, 'ANNUAL', '2023-02-20', '2023-02-22', 'WAIT', '이유', '경조사', '기타', '2023-02-20 04:30:00'),"+
                    "(10, 'ANNUAL', '2023-09-05', '2023-09-10', 'APPROVE', '이유', '병가', '기타', '2023-08-29 05:45:00'),"+
                    "(1, 'ANNUAL', '2023-08-15', '2023-08-20', 'WAIT', '이유', '경조사', '기타', '2023-08-15 09:00:00'),"+
                    "(2, 'DUTY', '2023-01-10', '2023-01-10', 'APPROVE', NULL, NULL, NULL, '2023-01-10 10:15:00'),"+
                    "(3, 'ANNUAL', '2023-03-25', '2023-03-27', 'REJECT', '이유', '병가', '기타', '2023-03-25 11:30:00'),"+
                    "(4, 'ANNUAL', '2023-06-05', '2023-06-10', 'APPROVE', '이유', '생리휴가', '기타', '2023-06-05 12:45:00'),"+
                    "(5, 'DUTY', '2023-01-19', '2023-01-19', 'WAIT', NULL, NULL, NULL, '2023-01-18 14:00:00'),"+
                    "(6, 'ANNUAL', '2023-11-05', '2023-11-06', 'APPROVE', '이유', '출산휴가', '기타', '2023-10-28 15:15:00'),"+
                    "(7, 'ANNUAL', '2023-12-20', '2023-12-23', 'REJECT', '이유', '경조사', '기타', '2023-11-18 16:30:00'),"+
                    "(8, 'ANNUAL', '2023-02-05', '2023-02-10', 'WAIT', '이유', '병가', '기타', '2023-01-28 17:45:00'),"+
                    "(9, 'DUTY', '2023-01-20', '2023-01-20', 'WAIT', NULL, NULL, NULL, '2023-01-18 18:00:00'),"+
                    "(10, 'ANNUAL', '2023-10-05', '2023-10-06', 'WAIT', '이유', '생리휴가', '기타', '2023-10-05 19:15:00'),"+
                    "(1, 'DUTY', '2023-04-10', '2023-04-10', 'APPROVE', NULL, NULL, NULL, '2023-04-10 20:30:00'),"+
                    "(2, 'ANNUAL', '2023-11-04', '2023-11-07', 'APPROVE', '이유', '출산휴가', '기타', '2023-11-01 21:45:00'),"+
                    "(3, 'ANNUAL', '2023-08-20', '2023-08-23', 'WAIT', '이유', '경조사', '기타', '2023-08-20 22:00:00'),"+
                    "(4, 'ANNUAL', '2023-05-19', '2023-05-22', 'REJECT', '이유', '병가', '기타', '2023-05-05 23:15:00'),"+
                    "(5, 'DUTY', '2023-03-01', '2023-03-01', 'APPROVE', NULL, NULL, NULL, '2023-02-25 00:30:00'),"+
                    "(6, 'ANNUAL', '2023-04-08', '2023-04-10', 'WAIT', '이유', '생리휴가', '기타', '2023-03-29 01:45:00'),"+
                    "(7, 'DUTY', '2023-08-20', '2023-08-20', 'APPROVE', NULL, NULL, NULL, '2023-08-20 02:00:00'),"+
                    "(8, 'ANNUAL', '2023-03-28', '2023-04-01', 'WAIT', '이유', '출산휴가', '기타', '2023-03-25 03:15:00'),"+
                    "(9, 'ANNUAL', '2023-07-14', '2023-07-18', 'WAIT', '이유', '경조사', '기타', '2023-07-05 04:30:00'),"+
                    "(10, 'ANNUAL', '2023-08-05', '2023-08-07', 'APPROVE', '이유', '병가', '기타', '2023-01-29 05:45:00');";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initApproveDate() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
             String query = "INSERT INTO approve_date_tb (order_id, user_id, date) VALUES " +
                     "(2,2,'2023-08-10'),"+ "(4,4,'2023-09-05'),"+ "(4,4,'2023-09-06'),"+
                     "(4,4,'2023-09-07'),"+ "(4,4,'2023-09-08'),"+ "(4,4,'2023-09-09'),"+
                     "(4,4,'2023-09-10'),"+ "(6,6,'2023-10-05'),"+ "(6,6,'2023-10-06'),"+
                     "(12,2,'2023-01-05'),"+ "(12,2,'2023-01-06'),"+ "(12,2,'2023-01-07'),"+
                     "(12,2,'2023-01-08'),"+ "(12,2,'2023-01-09'),"+ "(12,2,'2023-01-10'),"+
                     "(15,5,'2023-02-20'),"+ "(17,7,'2023-03-20'),"+ "(20,10,'2023-05-05'),"+
                     "(20,10,'2023-05-06'),"+ "(20,10,'2023-05-07'),"+ "(20,10,'2023-05-08'),"+
                     "(20,10,'2023-05-09'),"+ "(20,10,'2023-05-10'),"+ "(21,1,'2023-11-20'),"+
                     "(22,2,'2023-05-05'),"+ "(22,2,'2023-05-06'),"+ "(22,2,'2023-05-07'),"+
                     "(22,2,'2023-05-08'),"+ "(22,2,'2023-05-09'),"+ "(25,5,'2023-07-20'),"+
                     "(27,7,'2023-01-20'),"+ "(30,10,'2023-09-05'),"+ "(30,10,'2023-09-06'),"+
                     "(30,10,'2023-09-07'),"+ "(30,10,'2023-09-08'),"+ "(30,10,'2023-09-09'),"+
                     "(30,10,'2023-09-10'),"+ "(32,2,'2023-01-10'),"+ "(34,4,'2023-06-05'),"+
                     "(34,4,'2023-06-06'),"+ "(34,4,'2023-06-07'),"+ "(34,4,'2023-06-08'),"+
                     "(34,4,'2023-06-09'),"+ "(34,4,'2023-06-10'),"+ "(36,6,'2023-11-05'),"+
                     "(41,1,'2023-04-10'),"+ "(42,2,'2023-11-04'),"+ "(42,2,'2023-11-05'),"+
                     "(42,2,'2023-11-06'),"+ "(42,2,'2023-11-07'),"+ "(45,5,'2023-03-01'),"+
                     "(47,7,'2023-08-20'),"+ "(50,10,'2023-08-05'),"+ "(50,10,'2023-08-06'),"+
                     "(50,10,'2023-08-07');";
             statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
