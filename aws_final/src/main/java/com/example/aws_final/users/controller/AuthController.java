package com.example.aws_final.users.controller;


import com.example.aws_final.users.dto.request.LoginRequestDto;
import com.example.aws_final.users.dto.request.UserRegistryRequestDto;
import com.example.aws_final.users.dto.response.AuthResponseDto;
import com.example.aws_final.users.dto.response.UserResponseDto;
import com.example.aws_final.users.service.AuthService;
import com.example.aws_final.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

///
/// RECIBO JSONS -> SERVICE y LE REGRESO AL FRONT EL REPSONSE
@RestController
@RequestMapping("api/users") // establece el titulo principal de la api
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    ///  REGISTRY
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistryRequestDto request){
        return new ResponseEntity<>(userService.userRegister(request), HttpStatus.CREATED);
    }

    ///  LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request){
        return ResponseEntity.ok(authService.login(request));
    }
}
