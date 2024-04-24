package kr.co.felici.remembering.service;

/**
 * author: felici
 */


import kr.co.felici.remembering.domain.User;
import kr.co.felici.remembering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        log.info("----------------loadUserByUsername-------------------");
        Optional<User> _siteUser = userRepository.findByEmail(email);
        if (_siteUser.isEmpty()) {
            log.info("----------------UsernameNotFoundException(\"사용자를 찾을수 없습니다.\")-------------------");
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        User siteUser = _siteUser.get();
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        if ("admin".equals(username)) {
//            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
//        }
        return User.builder()
                .email(siteUser.getEmail())
                .password(siteUser.getPassword())
                .build();


//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}

