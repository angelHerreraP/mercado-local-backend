package com.example.aws_final.users.service;


import com.example.aws_final.roles.model.Role;
import com.example.aws_final.roles.repository.RoleRepository;
import com.example.aws_final.shared.exception.AppException;
import com.example.aws_final.shared.exception.ErrorCodes;
import com.example.aws_final.users.dto.request.UserRegistryRequestDto;
import com.example.aws_final.users.dto.response.UserResponseDto;
import com.example.aws_final.users.model.User;
import com.example.aws_final.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository; //te traes al repository
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;



    // registra un usuario
    public UserResponseDto userRegister(UserRegistryRequestDto userRequestDto){

        // VALIDACIONES DE QUE NO VENGA VACIO
        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new AppException(ErrorCodes.USER_EXISTS); // we've created this exception
        }
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new AppException(ErrorCodes.EMAIL_EXISTS); // we've created this exception
        }

        // HASHEA LA PSW
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        // ASIGNA UN ROL
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));


        //convertimos el DTO en objetos de la DB
        User user = new User(
                userRequestDto.getUsername(),
                userRequestDto.getEmail(),
                encodedPassword, // Aquí ya va cifrada
                userRole        // Le asignas el rol de una vez
        );
        userRepository.save(user);
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Usuario creado exitosamente")
                .build();
    }

    public UserResponseDto getByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Perfil encontrado!")
                .build();
    }

    @Transactional //pa que guardes los cambios en la DB
    public UserResponseDto updateRole(Integer userId, String roleName){
        //busca al usr a actualizar
        User usr = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

        //busca ahora el rol
        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCodes.NOT_FOUND));

        //cambiamos
        usr.setRole(newRole);
        userRepository.save(usr);

        return UserResponseDto.builder()
                .id(usr.getId())
                .username(usr.getUsername())
                .message("Role actualizado a " + newRole)
                .build();
    }

    //List all users
    public List<UserResponseDto> getAllUsers(){
        List<User> users = userRepository.findAll();

        /// Convierte tus users a DTO
        return users.stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole().getName())
                        .build())
                .collect(Collectors.toList());
    }

}
