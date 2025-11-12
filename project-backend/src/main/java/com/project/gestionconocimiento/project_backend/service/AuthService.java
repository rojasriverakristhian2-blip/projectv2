package com.project.gestionconocimiento.project_backend.service;

import org.springframework.stereotype.Service;

import com.project.gestionconocimiento.project_backend.model.Usuario;
import com.project.gestionconocimiento.project_backend.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean login(String correo, String contrasena, String rol) {
        Optional<Usuario> userOpt = usuarioRepository.findByCorreo(correo);
        // Verifica que el usuario exista
        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
    
            // Compara contraseña y rol
            return usuario.getContrasena().equals(contrasena)
                    && usuario.getRol().equalsIgnoreCase(rol);
        }
    
        return false; // No existe el usuario
    }
    
}
