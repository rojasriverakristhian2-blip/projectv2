package com.project.gestionconocimiento.project_backend.controller;

import com.project.gestionconocimiento.project_backend.model.Actividad;
import com.project.gestionconocimiento.project_backend.service.ActividadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/actividades")
@CrossOrigin(origins = "*")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    public List<Actividad> getAll() {
        return actividadService.getAllActividadesSPARQL()   ;
    }

    @GetMapping("/{id}")
    public Actividad getActividadPorId(@PathVariable long id) {
        return actividadService.getActividadPorIdSPARQL(id);
    }

    @PostMapping
    public void add(@RequestBody Actividad actividad) throws IOException {
        actividadService.addActividadSPARQL(actividad);;
    }

    @DeleteMapping("/{id}")
    public String deleteActividad(@PathVariable long id) throws IOException {
        boolean eliminado = actividadService.deleteActividadSPARQL(id);
        if (eliminado) {
            return ("Actividad eliminada correctamente");
        }
        return ("No se encontró la actividad con ID " + id);
    }

    @PutMapping("/{id}")
    public String updateActividad(@PathVariable long id, @RequestBody Actividad actividad) throws IOException {
        boolean actualizado = actividadService.updateActividadSPARQL(id, actividad);
        if (actualizado) {
            return "✅ Actividad actualizada correctamente";
        }
        return "❌ No se encontró la actividad con ID " + id;
    }

}
