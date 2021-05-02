package com.dodo.gobz.services;

import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.models.User;
import com.dodo.gobz.repositories.UserRepository;
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
