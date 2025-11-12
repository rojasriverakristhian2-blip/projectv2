package com.project.gestionconocimiento.project_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.project.gestionconocimiento.project_backend.model.Usuario;
import com.project.gestionconocimiento.project_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🟢 Endpoint para registrar usuarios
   // @PostMapping("/register")
  //  public Usuario register(@RequestBody Usuario usuario) {
  //      return authService.register(usuario);
  //  }

    // 🟢 Endpoint para login
    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        boolean success = authService.login(usuario.getCorreo(), usuario.getContrasena(), usuario.getRol());
        return success ? "Login exitoso" : "Login fallido";
    }
    
}
