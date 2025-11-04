package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.UserProfileResponse;
import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            UserProfileResponse response = new UserProfileResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getTelefono(),
                userDetails.getDireccion(),
                userDetails.getCiudad(),
                userDetails.getPais(),
                userDetails.getProfilePhoto()
            );
            return ResponseEntity.ok(ApiResponse.success(response));
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error("No autenticado"));
        }
    }
}
