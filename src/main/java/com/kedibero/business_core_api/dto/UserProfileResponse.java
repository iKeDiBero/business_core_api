package com.kedibero.business_core_api.dto;

public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String profilePhoto;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String username, String email, String telefono, String direccion, String ciudad, String pais, String profilePhoto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.profilePhoto = profilePhoto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
}

