package fast.mini.be.domain.user;

import fast.mini.be.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@DynamicInsert
@Table(name = "user_tb")
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false) // 단방향(Bcrypt) 암호화하여 저장
    private String password;

    @Column(nullable = false, length = 30) // AES256 양방향 암호화하여 저장
    private String empName;

    @Column(nullable = false, length = 8) // 2023@@@@(id값)
    private String empNo;

    @Column(length = 5)
    private String position;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int annualCount; // 스케줄러 사용하여 한달기준으로 +1부여?
}
