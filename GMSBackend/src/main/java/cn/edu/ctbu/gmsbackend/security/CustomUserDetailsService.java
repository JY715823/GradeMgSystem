package cn.edu.ctbu.gmsbackend.security;

import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import cn.edu.ctbu.gmsbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String key) throws UsernameNotFoundException {
        // key format: role|username
        String[] parts = key.split("\\|");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("invalid key");
        }
        UserRole role = UserRole.valueOf(parts[0]);
        String username = parts[1];
        UserAccount user = userAccountRepository.findByUsernameAndRole(username, role)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new CustomUserDetails(user);
    }
}
