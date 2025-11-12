package com.project.gestionconocimiento.project_backend.controller;

import com.project.gestionconocimiento.project_backend.model.Reunion;
import com.project.gestionconocimiento.project_backend.model.Usuario;
import com.project.gestionconocimiento.project_backend.service.ReunionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;



@RestController
@RequestMapping("/api/reuniones")
@CrossOrigin(origins = "*")
public class ReunionController {

    @Autowired
    private ReunionService reunionService;

    @GetMapping
    public List<Reunion> getAll() {
        return reunionService.getAllReunionesSPARQL();
    }

    @GetMapping("/{id}")
    public Reunion getReunionPorId(@PathVariable long id) {
        System.out.println("#############################" + id);
        return reunionService.getReunionPorIdSPARQL(id);
    }

    @PostMapping
    public void add(@RequestBody Reunion reunion) throws IOException {
        reunionService.addReunionSPARQL(reunion);
    }

    @DeleteMapping("/{id}")
    public String deleteReunion(@PathVariable long id) throws IOException {
        boolean eliminado = reunionService.deleteReunionSPARQL(id);
        if (eliminado) {
            return "Reunión eliminada correctamente";
        }
        return "No se encontró la reunión con ID " + id;
    }

    @PutMapping("/{id}")
    public String updateReunion(@PathVariable long id, @RequestBody Reunion reunion) throws IOException {
        boolean actualizado = reunionService.updateReunionSPARQL(id, reunion);
        if (actualizado) {
            return "✅ Reunión actualizada correctamente";
        }
        return "❌ No se encontró la reunión con ID " + id;
    }

    @PostMapping("/{id}/confirmar-asistencia")
    public ResponseEntity<String> confirmarAsistencia(@PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        System.out.println("confirmarAsistencia#####################" + id);

        Object cedulaObj = request.get("cedula");
        if (cedulaObj == null) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("❌ La cédula es requerida");
        }

        Integer cedula;
        try {
            if (cedulaObj instanceof Integer) {
                cedula = (Integer) cedulaObj;
            } else if (cedulaObj instanceof String) {
                cedula = Integer.parseInt((String) cedulaObj);
            } else if (cedulaObj instanceof Number) {
                cedula = ((Number) cedulaObj).intValue();
            } else {
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("❌ Formato de cédula inválido");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("❌ La cédula debe ser un número válido");
        }

        String resultado = reunionService.confirmarAsistenciaPorCedula(id, cedula);
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(resultado);
    }

    @GetMapping("/{id}/confirmados")
    public List<Usuario> getUsuariosConfirmados(@PathVariable Integer id) {
        return reunionService.getUsuariosConfirmados(id);
    }

    @GetMapping("/{id}/no-confirmados")
    public List<Usuario> getUsuariosNoConfirmados(@PathVariable Integer id) {
        return reunionService.getUsuariosNoConfirmados(id);
    }
}
