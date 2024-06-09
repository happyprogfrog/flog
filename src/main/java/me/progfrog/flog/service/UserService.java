package me.progfrog.flog.service;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.domain.User;
import me.progfrog.flog.dto.user.UserReqAddDto;
import me.progfrog.flog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(UserReqAddDto reqAddDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        userRepository.save(new User(
                reqAddDto.email(),
                encoder.encode(reqAddDto.password()))
        );
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
