package com.example.aws_final.users.dto.response;

import com.example.aws_final.roles.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private int id; /// TODO: Review si si debe ir aqui
    private String username;
    private String email;
    private String role;
    private String message; /// El response de 'Logeado con Exito'
}
