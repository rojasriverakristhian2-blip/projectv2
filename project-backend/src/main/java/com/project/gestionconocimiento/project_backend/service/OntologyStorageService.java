package com.project.gestionconocimiento.project_backend.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class OntologyStorageService {
    
    private String currentOntology = null;
    
    public String getSavedOntology() {
        return currentOntology;
    }
    
    public void saveOntology(String ontologyContent) {
        this.currentOntology = ontologyContent;
    }
    
    @PostConstruct
    public void loadInitialOntology() {
        try {
            // Cargar ontología inicial desde classpath
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ontology/JuntaAccionComunal.ttl");
            if (inputStream != null) {
                currentOntology = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                inputStream.close();
                System.out.println("✅ Ontología inicial cargada en memoria");
            }
        } catch (Exception e) {
            System.err.println("❌ Error cargando ontología inicial: " + e.getMessage());
        }
    }
}
