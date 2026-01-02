package cn.edu.ctbu.gmsbackend.repository;

import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsernameAndRole(String username, UserRole role);
}
