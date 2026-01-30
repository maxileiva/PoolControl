package com.example.usuarios.dto;

public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contrasena;
    private String rolNombre;

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getContrasena() { return contrasena; }
    public String getRolNombre() { return rolNombre; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
}