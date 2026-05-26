package com.codemortem.service;

import com.codemortem.dto.SignUpRequestDTO;
import com.codemortem.entity.User;
import com.codemortem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.codemortem.config.JwtUtil;
import com.codemortem.dto.AuthResponseDTO;
import com.codemortem.dto.LoginRequestDTO;
import com.codemortem.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public String signup(SignUpRequestDTO dto){

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponseDTO login(
            LoginRequestDTO dto
    ){

        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPassword()
                );

        if(!passwordMatches) {

            throw new ResourceNotFoundException(
                    "Invalid email or password"
            );
        }

            String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(token);
    }
}
