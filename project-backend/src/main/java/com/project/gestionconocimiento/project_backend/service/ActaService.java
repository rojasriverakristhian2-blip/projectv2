package com.project.gestionconocimiento.project_backend.service;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.gestionconocimiento.project_backend.model.Acta;

import org.apache.jena.update.UpdateAction;

import java.io.*;
import java.util.*;

@Service
public class ActaService {

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
                System.out.println("✅ Ontología cargada desde almacenamiento (ActaService)");
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
            System.out.println("✅ Ontología guardada en almacenamiento (ActaService)");
            
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

    public List<Acta> getAllActasSPARQL() {
        List<Acta> actas = new ArrayList<>();
        Model model = loadModel();

        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema/>
            SELECT ?id ?desicion
            WHERE {
              ?acta a :Acta .
              ?acta :ActaID ?id .
              ?acta :ActaDesicion ?desicion .
            }
        """;

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Acta acta = new Acta();

                acta.setActaID(Integer.parseInt(sol.getLiteral("id").getString()));
                acta.setActaDesicion(sol.getLiteral("desicion").getString());

                actas.add(acta);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en consulta SPARQL de actas: " + e.getMessage());
            e.printStackTrace();
        }
        return actas;
    }

    public void addActaSPARQL(Acta acta) throws IOException {
        Model model = loadModel();

        String updateQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            INSERT DATA {
              :Acta%d a :Acta ;
                  :ActaID "%d" ;
                  :ActaDesicion "%s" .
            }
        """.formatted(
            acta.getActaID(),
            acta.getActaID(),
            escapeString(acta.getActaDesicion())
        );

        UpdateAction.parseExecute(updateQuery, model);
        saveModel(model);
    }

    public boolean deleteActaSPARQL(long id) throws IOException {
        Model model = loadModel();

        String deleteQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE WHERE {
              :Acta%d ?p ?o .
            }
        """.formatted(id);

        UpdateAction.parseExecute(deleteQuery, model);
        saveModel(model);
        return true;
    }

    public Acta getActaPorIdSPARQL(long id) {
        Model model = loadModel();
        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            SELECT ?desicion
            WHERE {
              ?acta a :Acta .
              ?acta :ActaID ?id .
              ?acta :ActaDesicion ?desicion .
              FILTER(?id = %d)
            }
        """.formatted(id);

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Acta acta = new Acta();
                acta.setActaID((int) id);
                acta.setActaDesicion(sol.getLiteral("desicion").getString());
                return acta;
            }
        } catch (Exception e) {
            System.err.println("❌ Error consultando acta por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateActaSPARQL(long id, Acta acta) throws IOException {
        Model model = loadModel();

        String deleteOldValues = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE {
              :Acta%d :ActaDesicion ?d .
            }
            INSERT {
              :Acta%d :ActaDesicion "%s" .
            }
            WHERE {
              OPTIONAL { :Acta%d :ActaDesicion ?d }
            }
        """.formatted(id, id, escapeString(acta.getActaDesicion()), id);

        UpdateAction.parseExecute(deleteOldValues, model);
        saveModel(model);
        return true;
    }
}