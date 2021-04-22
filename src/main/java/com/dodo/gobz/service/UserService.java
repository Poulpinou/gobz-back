package com.dodo.gobz.service;

import com.dodo.gobz.exception.ResourceNotFoundException;
import com.dodo.gobz.model.User;
import com.dodo.gobz.repository.UserRepository;
import com.dodo.gobz.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserFromPrincipal(UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userPrincipal.getId()));
    }
}
