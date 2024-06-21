package org.myblog.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /*
        CRUD 모음
     */

    // User 등록
    @Transactional
    public User saveUser(User user){
        return repository.save(user); // User 없다면 insert, 있다면 update
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        return repository.findById(id).orElse(null);
    }

    /*
        CRUD 기타 비즈니스 로직 모음
     */
    // 회원정보 수정, 삭제를 위한 비밀번호 체크
    public boolean CheckPassword(String password, String passwordCheck){
        return password.equals(passwordCheck);
    }

    // 아이디 중복 검사
    public boolean isUsernameDuplicate(String username){
        return repository.existsByUsername(username);
    }

    // 이메일 중복 검사
    public boolean isEmailDuplicate(String email){
        return repository.existsByEmail(email);
    }

    // 로그인 성공 여부
    public UserLoginForm login(String username, String password){
        // 데이터베이스에서 사용자 정보를 조회하고 + 비밀번호가 일치하면, 로그인 성공!
        User foundUser = repository.findByUsername(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            UserLoginForm userLoginForm = new UserLoginForm();
            userLoginForm.setUsername(foundUser.getUsername());
            userLoginForm.setPassword(foundUser.getPassword());

            return userLoginForm; // 로그인 성공
        }

        return null; // 로그인 실패
    }
}
