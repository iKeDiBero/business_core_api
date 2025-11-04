package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.AuthResponse;
import com.kedibero.business_core_api.dto.LoginRequest;
import com.kedibero.business_core_api.dto.RegisterRequest;
import com.kedibero.business_core_api.entity.Role;
import com.kedibero.business_core_api.entity.User;
import com.kedibero.business_core_api.repository.RoleRepository;
import com.kedibero.business_core_api.repository.UserRepository;
import com.kedibero.business_core_api.security.JwtTokenProvider;
import com.kedibero.business_core_api.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getTelefono(),
                userDetails.getDireccion(),
                userDetails.getCiudad(),
                userDetails.getPais(),
                userDetails.getProfilePhoto()
        );
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso");
        }

        // Crear nuevo usuario
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        // Asignar campos adicionales
        user.setTelefono(registerRequest.getTelefono());
        user.setDireccion(registerRequest.getDireccion());
        user.setCiudad(registerRequest.getCiudad());
        user.setPais(registerRequest.getPais());
        user.setProfilePhoto(registerRequest.getProfilePhoto());

        // Asignar rol USER por defecto
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "Usuario registrado exitosamente";
    }

    public String logout() {
        SecurityContextHolder.clearContext();
        return "Sesión cerrada exitosamente";
    }
}

