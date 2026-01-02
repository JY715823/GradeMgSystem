package cn.edu.ctbu.gmsbackend.auth;

import cn.edu.ctbu.gmsbackend.common.BusinessException;
import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import cn.edu.ctbu.gmsbackend.repository.UserAccountRepository;
import cn.edu.ctbu.gmsbackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        UserAccount account = userAccountRepository.findByUsernameAndRole(request.getUsername(), request.getRole())
                .orElseThrow(() -> new BusinessException(4010, "用户不存在"));
        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new BusinessException(4010, "密码错误");
        }
        String token = jwtUtil.generateToken(account.getId(), account.getRole(), account.getUsername());
        return new LoginResponse(token, account.getId(), account.getRole(), account.getUsername());
    }

    public void resetPassword(UserAccount account, String newPassword) {
        account.setPasswordHash(passwordEncoder.encode(newPassword));
        userAccountRepository.save(account);
    }

    public UserAccount createUser(String username, UserRole role, String profileType, Long profileId, String password) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setRole(role);
        account.setProfileType(profileType);
        account.setProfileId(profileId);
        account.setPasswordHash(passwordEncoder.encode(password));
        return userAccountRepository.save(account);
    }
}
