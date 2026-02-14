package com.example.aws_final.users.controller;

import com.example.aws_final.users.dto.response.UserResponseDto;
import com.example.aws_final.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me") // Bering my data (SHOW MY PROFILE)
    public ResponseEntity<UserResponseDto> getMyProfile(Principal principal){ // Principal es un objeto de Spring que trae l user name del token
        String username = principal.getName();
        return ResponseEntity.ok(userService.getByUsername(username));

    }


    ///  MODIFY ROLE USER
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPERUSER')") /// SOLO EL MERO MERO EDITA
    public ResponseEntity<UserResponseDto> editRole(
            @PathVariable Integer id,
            @RequestParam String roleName
    ){
        return ResponseEntity.ok(userService.updateRole(id, roleName));
    }

    /// BRING ALL USERS
    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPERUSER')") /// Solo el mero mero puede ver
    public ResponseEntity<List<UserResponseDto>> listAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
