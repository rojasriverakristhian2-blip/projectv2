package com.project.gestionconocimiento.project_backend.service;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.gestionconocimiento.project_backend.model.Asistencia;

import org.apache.jena.update.UpdateAction;

import java.io.*;
import java.util.*;

@Service
public class AsistenciaService {

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
                System.out.println("✅ Ontología cargada desde almacenamiento (AsistenciaService)");
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
            System.out.println("✅ Ontología guardada en almacenamiento (AsistenciaService)");
            
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

    public List<Asistencia> getAllAsistenciasSPARQL() {
        List<Asistencia> asistencias = new ArrayList<>();
        Model model = loadModel();

        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema/>
            SELECT ?id ?participantes
            WHERE {
              ?asistencia a :Asistencia .
              ?asistencia :AsistenciaID ?id .
              ?asistencia :AsistenciaParticipantes ?participantes .
            }
        """;

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Asistencia asistencia = new Asistencia();

                asistencia.setAsistenciaID(Integer.parseInt(sol.getLiteral("id").getString()));
                asistencia.setAsistenciaParticipantes(sol.getLiteral("participantes").getString());

                asistencias.add(asistencia);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en consulta SPARQL de asistencias: " + e.getMessage());
            e.printStackTrace();
        }
        return asistencias;
    }

    public void addAsistenciaSPARQL(Asistencia asistencia) throws IOException {
        Model model = loadModel();

        String updateQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            INSERT DATA {
              :Asistencia%d a :Asistencia ;
                  :AsistenciaID "%d" ;
                  :AsistenciaParticipantes "%s" .
            }
        """.formatted(
            asistencia.getAsistenciaID(),
            asistencia.getAsistenciaID(),
            escapeString(asistencia.getAsistenciaParticipantes())
        );

        UpdateAction.parseExecute(updateQuery, model);
        saveModel(model);
    }

    public boolean deleteAsistenciaSPARQL(long id) throws IOException {
        Model model = loadModel();

        String deleteQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE WHERE {
              :Asistencia%d ?p ?o .
            }
        """.formatted(id);

        UpdateAction.parseExecute(deleteQuery, model);
        saveModel(model);
        return true;
    }

    public Asistencia getAsistenciaPorIdSPARQL(long id) {
        Model model = loadModel();
        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            SELECT ?participantes
            WHERE {
              ?asistencia a :Asistencia .
              ?asistencia :AsistenciaID ?id .
              ?asistencia :AsistenciaParticipantes ?participantes .
              FILTER(?id = %d)
            }
        """.formatted(id);

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Asistencia asistencia = new Asistencia();
                asistencia.setAsistenciaID((int) id);
                asistencia.setAsistenciaParticipantes(sol.getLiteral("participantes").getString());
                return asistencia;
            }
        } catch (Exception e) {
            System.err.println("❌ Error consultando asistencia por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAsistenciaSPARQL(long id, Asistencia asistencia) throws IOException {
        Model model = loadModel();

        String deleteOldValues = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE {
              :Asistencia%d :AsistenciaParticipantes ?p .
            }
            INSERT {
              :Asistencia%d :AsistenciaParticipantes "%s" .
            }
            WHERE {
              OPTIONAL { :Asistencia%d :AsistenciaParticipantes ?p }
            }
        """.formatted(id, id, escapeString(asistencia.getAsistenciaParticipantes()), id);

        UpdateAction.parseExecute(deleteOldValues, model);
        saveModel(model);
        return true;
    }
}