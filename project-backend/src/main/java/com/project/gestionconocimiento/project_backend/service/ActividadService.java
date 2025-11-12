package com.project.gestionconocimiento.project_backend.service;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.gestionconocimiento.project_backend.model.Actividad;

import org.apache.jena.update.UpdateAction;

import java.io.*;
import java.util.*;

@Service
public class ActividadService {

    @Autowired
    private OntologyStorageService ontologyStorageService;

    /**
     * Carga el modelo desde el servicio de almacenamiento
     */
    private Model loadModel() {
        try {
            String ontologyContent = ontologyStorageService.getSavedOntology();
            if (ontologyContent != null && !ontologyContent.trim().isEmpty()) {
                Model model = ModelFactory.createDefaultModel();
                model.read(new StringReader(ontologyContent), null, "TTL");
                System.out.println("✅ Ontología cargada desde almacenamiento (ActividadService)");
                return model;
            } else {
                throw new RuntimeException("No se pudo cargar la ontología - contenido vacío");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error cargando ontología: " + e.getMessage(), e);
        }
    }

    /**
     * Guarda el modelo en el servicio de almacenamiento
     */
    private void saveModel(Model model) {
        try {
            StringWriter writer = new StringWriter();
            RDFDataMgr.write(writer, model, org.apache.jena.riot.Lang.TTL);
            String ontologyContent = writer.toString();
            
            ontologyStorageService.saveOntology(ontologyContent);
            System.out.println("✅ Ontología guardada en almacenamiento (ActividadService)");
            
        } catch (Exception e) {
            throw new RuntimeException("Error guardando ontología: " + e.getMessage(), e);
        }
    }

    /**
     * Escapa caracteres especiales en strings para SPARQL
     */
    private String escapeString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    public List<Actividad> getAllActividadesSPARQL() {
        List<Actividad> actividades = new ArrayList<>();
        Model model = loadModel();
    
        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema/>
            SELECT ?id ?nombre ?fecha ?descripcion
            WHERE {
              ?actividad a :Actividad .
              ?actividad :ActividadID ?id .
              ?actividad :ActividadNombre ?nombre .
              ?actividad :ActividadFecha ?fecha .
              ?actividad :ActividadDescripcion ?descripcion .
            }
              
        """;
    
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
    
            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Actividad act = new Actividad();
    
                act.setActividadID(Integer.parseInt(sol.getLiteral("id").getString()));
                act.setActividadNombre(sol.getLiteral("nombre").getString());
                act.setActividadFecha(sol.getLiteral("fecha").getString());
                act.setActividadDescripcion(sol.getLiteral("descripcion").getString());
    
                actividades.add(act);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en consulta SPARQL de actividades: " + e.getMessage());
            e.printStackTrace();
        }
        return actividades;
    }

    public void addActividadSPARQL(Actividad act) throws IOException {
        Model model = loadModel();
    
        String updateQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            INSERT DATA {
              :Actividad%d a :Actividad ;
                  :ActividadID "%d" ;
                  :ActividadNombre "%s" ;
                  :ActividadFecha "%s" ;
                  :ActividadDescripcion "%s" .
            }
        """.formatted(
            act.getActividadID(),
            act.getActividadID(),
            escapeString(act.getActividadNombre()),
            escapeString(act.getActividadFecha()),
            escapeString(act.getActividadDescripcion())
        );
    
        UpdateAction.parseExecute(updateQuery, model);
        saveModel(model);
    }

    public boolean deleteActividadSPARQL(long id) throws IOException {
        Model model = loadModel();
    
        String deleteQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE WHERE {
              :Actividad%d ?p ?o .
            }
        """.formatted(id);
    
        UpdateAction.parseExecute(deleteQuery, model);
        saveModel(model);
        return true;
    }

    public Actividad getActividadPorIdSPARQL(long id) {
        Model model = loadModel();
        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            SELECT ?nombre ?fecha ?descripcion
            WHERE {
              ?actividad a :Actividad .
              ?actividad :ActividadID ?id .
              ?actividad :ActividadNombre ?nombre .
              ?actividad :ActividadFecha ?fecha .
              ?actividad :ActividadDescripcion ?descripcion .
              FILTER(?id = %d)
            }
        """.formatted(id);
    
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Actividad act = new Actividad();
                act.setActividadID((int) id);
                act.setActividadNombre(sol.getLiteral("nombre").getString());
                act.setActividadFecha(sol.getLiteral("fecha").getString());
                act.setActividadDescripcion(sol.getLiteral("descripcion").getString());
                return act;
            }
        } catch (Exception e) {
            System.err.println("❌ Error consultando actividad por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateActividadSPARQL(long id, Actividad act) throws IOException {
        Model model = loadModel();
    
        String deleteOldValues = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE {
              :Actividad%d :ActividadNombre ?n ;
                            :ActividadFecha ?f ;
                            :ActividadDescripcion ?d .
            }
            INSERT {
              :Actividad%d :ActividadNombre "%s" ;
                            :ActividadFecha "%s" ;
                            :ActividadDescripcion "%s" .
            }
            WHERE {
              OPTIONAL { :Actividad%d :ActividadNombre ?n }
              OPTIONAL { :Actividad%d :ActividadFecha ?f }
              OPTIONAL { :Actividad%d :ActividadDescripcion ?d }
            }
        """.formatted(id, id, 
            escapeString(act.getActividadNombre()), 
            escapeString(act.getActividadFecha()), 
            escapeString(act.getActividadDescripcion()), 
            id, id, id);
    
        UpdateAction.parseExecute(deleteOldValues, model);
        saveModel(model);
        return true;
    }
}