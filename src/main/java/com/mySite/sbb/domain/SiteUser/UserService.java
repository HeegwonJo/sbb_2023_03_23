package com.mySite.sbb.domain.SiteUser;

import com.mySite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public SiteUser createUser(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    public SiteUser getUser(String username){
        Optional<SiteUser> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user.get();
        }
        else throw new DataNotFoundException("유저가 없습니다.");
    }

}
