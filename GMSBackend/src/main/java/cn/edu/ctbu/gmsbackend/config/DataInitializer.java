package cn.edu.ctbu.gmsbackend.config;

import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import cn.edu.ctbu.gmsbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder; // 2. 注入密码加密器

    @Override
    public void run(String... args) {
        if (userAccountRepository.findByUsernameAndRole("admin", UserRole.ADMIN).isEmpty()) {
            UserAccount account = new UserAccount();
            account.setUsername("admin");
            account.setRole(UserRole.ADMIN);
            account.setProfileType("ADMIN");
            account.setProfileId(0L);
            account.setPasswordHash(passwordEncoder.encode("123456")); // 123456
            userAccountRepository.save(account);

            userAccountRepository.save(account);
            System.out.println("管理员用户已创建，密码为: 123456");
        }
    }
}
