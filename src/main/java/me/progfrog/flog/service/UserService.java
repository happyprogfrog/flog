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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(UserReqAddDto reqAddDto) {
        userRepository.save(new User(
                reqAddDto.email(),
                bCryptPasswordEncoder.encode(reqAddDto.password()))
        );
    }
}
