package cn.edu.ctbu.gmsbackend.config;

import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import cn.edu.ctbu.gmsbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;

    @Override
    public void run(String... args) {
        if (userAccountRepository.findByUsernameAndRole("admin", UserRole.ADMIN).isEmpty()) {
            UserAccount account = new UserAccount();
            account.setUsername("admin");
            account.setRole(UserRole.ADMIN);
            account.setProfileType("ADMIN");
            account.setProfileId(0L);
            account.setPasswordHash("$2a$10$raXIu0Z.8bN87UTrPBlEHW0ZVXxwR0Db50lHwpG7obcMHU6D0jZqa"); // 123456
            userAccountRepository.save(account);
        }
    }
}
