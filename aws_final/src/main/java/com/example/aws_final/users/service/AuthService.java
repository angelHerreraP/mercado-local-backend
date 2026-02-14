package com.example.aws_final.users.service;

import com.example.aws_final.security.service.JwtService;
import com.example.aws_final.shared.exception.AppException;
import com.example.aws_final.shared.exception.ErrorCodes;
import com.example.aws_final.users.dto.request.LoginRequestDto;
import com.example.aws_final.users.dto.response.AuthResponseDto;
import com.example.aws_final.users.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthResponseDto login(LoginRequestDto loginDto) {
                // vamos a pasar las credenciales, primero antes de validar user y password
                // estan bien
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(),
                                loginDto.getPassword()));

                /// busca el email
                var user = userRepository.findByUsername(loginDto.getUsername())
                                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

                // fabica el token
                String jwtToken = jwtService.generateToken(user.getUsername(), new HashMap<>());

                /// Armate con el BNuilder el DTO
                return AuthResponseDto.builder()
                                .id(user.getId())
                                .token(jwtToken)
                                .username(loginDto.getUsername())
                                .role(user.getRole().getName())
                                .type("Bearer")
                                .build();

        }
}
