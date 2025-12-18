package com.example.cheongyakassist.auth.service;

import com.example.cheongyakassist.user.entity.User;
import com.example.cheongyakassist.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User register(String email, String password, String name) {

        if(checkEmailDuplicate(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다!");
        }

        User user = new User(email, password, name);

        return userRepository.save(user);

    }

    public boolean checkEmailDuplicate(String email){
        return userRepository.existsByEmail(email);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다!"));
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!");
        }
        return user;
    }
}
