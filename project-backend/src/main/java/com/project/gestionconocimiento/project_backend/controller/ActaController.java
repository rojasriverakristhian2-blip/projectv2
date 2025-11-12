package com.project.gestionconocimiento.project_backend.controller;

import com.project.gestionconocimiento.project_backend.model.Acta;
import com.project.gestionconocimiento.project_backend.service.ActaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/actas")
public class ActaController {

    @Autowired
    private ActaService actaService;

    @GetMapping
    public List<Acta> getAll() {
        return actaService.getAllActasSPARQL();
    }

    @GetMapping("/{id}")
    public Acta getActaPorId(@PathVariable long id) {
        return actaService.getActaPorIdSPARQL(id);
    }

    @PostMapping
    public void add(@RequestBody Acta acta) throws IOException {
        actaService.addActaSPARQL(acta);
    }

    @DeleteMapping("/{id}")
    public String deleteActa(@PathVariable long id) throws IOException {
        boolean eliminado = actaService.deleteActaSPARQL(id);
        if (eliminado) {
            return "Acta eliminada correctamente";
        }
        return "No se encontró el acta con ID " + id;
    }

    @PutMapping("/{id}")
    public String updateActa(@PathVariable long id, @RequestBody Acta acta) throws IOException {
        boolean actualizado = actaService.updateActaSPARQL(id, acta);
        if (actualizado) {
            return "✅ Acta actualizada correctamente";
        }
        return "❌ No se encontró el acta con ID " + id;
    }
}

