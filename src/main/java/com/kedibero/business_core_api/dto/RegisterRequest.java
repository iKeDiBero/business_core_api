package com.kedibero.business_core_api.dto;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;
    private String profilePhoto;

    // Constructors
    public RegisterRequest() {}

    public RegisterRequest(String username, String email, String password,
                           String telefono, String direccion, String ciudad, String pais, String profilePhoto) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.profilePhoto = profilePhoto;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

