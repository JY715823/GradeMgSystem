package cn.edu.ctbu.gmsbackend.profile;

import cn.edu.ctbu.gmsbackend.auth.AuthService;
import cn.edu.ctbu.gmsbackend.common.BusinessException;
import cn.edu.ctbu.gmsbackend.entity.Student;
import cn.edu.ctbu.gmsbackend.entity.Teacher;
import cn.edu.ctbu.gmsbackend.entity.UserAccount;
import cn.edu.ctbu.gmsbackend.entity.UserRole;
import cn.edu.ctbu.gmsbackend.repository.StudentRepository;
import cn.edu.ctbu.gmsbackend.repository.TeacherRepository;
import cn.edu.ctbu.gmsbackend.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserAccountRepository userAccountRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    private UserAccount currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof cn.edu.ctbu.gmsbackend.security.CustomUserDetails userDetails)) {
            throw new BusinessException(4010, "未登录");
        }
        return userDetails.getUser();
    }

    public ProfileResponse getProfile() {
        UserAccount account = currentUser();
        if (account.getRole() == UserRole.STUDENT) {
            Student s = studentRepository.findById(account.getProfileId())
                    .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
            return new ProfileResponse(account.getId(), account.getRole(), account.getUsername(),
                    s.getName(), s.getClassName(), s.getPhone(), s.getEmail());
        } else if (account.getRole() == UserRole.TEACHER) {
            Teacher t = teacherRepository.findById(account.getProfileId())
                    .orElseThrow(() -> new BusinessException(4040, "教师不存在"));
            return new ProfileResponse(account.getId(), account.getRole(), account.getUsername(),
                    t.getName(), t.getDept(), t.getPhone(), t.getEmail());
        } else {
            return new ProfileResponse(account.getId(), account.getRole(), account.getUsername(),
                    "管理员", "教务", null, null);
        }
    }

    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        UserAccount account = currentUser();
        if (account.getRole() == UserRole.STUDENT) {
            Student s = studentRepository.findById(account.getProfileId())
                    .orElseThrow(() -> new BusinessException(4040, "学生不存在"));
            s.setName(request.getName());
            s.setClassName(request.getClassOrDept() != null ? request.getClassOrDept() : s.getClassName());
            s.setPhone(request.getPhone());
            s.setEmail(request.getEmail());
            studentRepository.save(s);
        } else if (account.getRole() == UserRole.TEACHER) {
            Teacher t = teacherRepository.findById(account.getProfileId())
                    .orElseThrow(() -> new BusinessException(4040, "教师不存在"));
            t.setName(request.getName());
            t.setDept(request.getClassOrDept() != null ? request.getClassOrDept() : t.getDept());
            t.setPhone(request.getPhone());
            t.setEmail(request.getEmail());
            teacherRepository.save(t);
        }
        return getProfile();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        UserAccount account = currentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPasswordHash())) {
            throw new BusinessException(4001, "原密码错误");
        }
        authService.resetPassword(account, request.getNewPassword());
    }
}
