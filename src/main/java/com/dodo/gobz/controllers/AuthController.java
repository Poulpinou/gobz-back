package com.dodo.gobz.controllers;

import com.dodo.gobz.exceptions.BadRequestException;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.common.AuthProvider;
import com.dodo.gobz.payloads.requests.LoginRequest;
import com.dodo.gobz.payloads.requests.SignUpRequest;
import com.dodo.gobz.payloads.responses.ApiResponse;
import com.dodo.gobz.payloads.responses.AuthResponse;
import com.dodo.gobz.repositories.UserRepository;
import com.dodo.gobz.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        final User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.local)
                .build();

        final User result = userRepository.save(user);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/me")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

}