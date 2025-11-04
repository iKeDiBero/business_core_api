package com.kedibero.business_core_api.security;

import com.kedibero.business_core_api.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
    // Campos adicionales
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String profilePhoto;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           boolean enabled, Collection<? extends GrantedAuthority> authorities,
                           String telefono, String direccion, String ciudad, String pais, String profilePhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.profilePhoto = profilePhoto;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                authorities,
                user.getTelefono(),
                user.getDireccion(),
                user.getCiudad(),
                user.getPais(),
                user.getProfilePhoto()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return enabled; }
    // Getters adicionales
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public String getProfilePhoto() { return profilePhoto; }
}

