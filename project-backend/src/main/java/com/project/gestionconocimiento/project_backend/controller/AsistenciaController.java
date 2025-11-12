package com.project.gestionconocimiento.project_backend.controller; 

import com.project.gestionconocimiento.project_backend.model.Asistencia;
import com.project.gestionconocimiento.project_backend.service.AsistenciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @GetMapping
    public List<Asistencia> getAll() {
        return asistenciaService.getAllAsistenciasSPARQL();
    }

    @GetMapping("/{id}")
    public Asistencia getAsistenciaPorId(@PathVariable long id) {
        return asistenciaService.getAsistenciaPorIdSPARQL(id);
    }

    @PostMapping
    public void add(@RequestBody Asistencia asistencia) throws IOException {
        asistenciaService.addAsistenciaSPARQL(asistencia);
    }

    @DeleteMapping("/{id}")
    public String deleteAsistencia(@PathVariable long id) throws IOException {
        boolean eliminado = asistenciaService.deleteAsistenciaSPARQL(id);
        if (eliminado) {
            return "Asistencia eliminada correctamente";
        }
        return "No se encontró la asistencia con ID " + id;
    }

    @PutMapping("/{id}")
    public String updateAsistencia(@PathVariable long id, @RequestBody Asistencia asistencia) throws IOException {
        boolean actualizado = asistenciaService.updateAsistenciaSPARQL(id, asistencia);
        if (actualizado) {
            return "✅ Asistencia actualizada correctamente";
        }
        return "❌ No se encontró la asistencia con ID " + id;
    }
}

