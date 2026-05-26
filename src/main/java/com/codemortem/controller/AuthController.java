package com.codemortem.controller;

import com.codemortem.dto.AuthResponseDTO;
import com.codemortem.dto.LoginRequestDTO;
import com.codemortem.dto.SignUpRequestDTO;
import com.codemortem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(

            @Valid
            @RequestBody SignUpRequestDTO dto
            ){

        return ResponseEntity.ok(
                authService.signup(dto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(

            @Valid
            @RequestBody LoginRequestDTO dto
            ){

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}
