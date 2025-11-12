package com.project.gestionconocimiento.project_backend.service;

import com.project.gestionconocimiento.project_backend.repository.UsuarioRepository;
import com.project.gestionconocimiento.project_backend.model.Acta;
import com.project.gestionconocimiento.project_backend.model.Asistencia;
import com.project.gestionconocimiento.project_backend.model.AsistenciaReunion;
import com.project.gestionconocimiento.project_backend.model.Reunion;
import com.project.gestionconocimiento.project_backend.model.Usuario;
import com.project.gestionconocimiento.project_backend.repository.AsistenciaReunionRepository;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.jena.update.UpdateAction;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReunionService {

    @Autowired
    private OntologyStorageService ontologyStorageService;

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsistenciaReunionRepository asistenciaReunionRepository;

    @Autowired
    private ActaService actaService;

    /**
     * Carga el modelo desde el servicio de almacenamiento
     */
    private Model loadModel() {
        try {
            String ontologyContent = ontologyStorageService.getSavedOntology();
            if (ontologyContent != null && !ontologyContent.trim().isEmpty()) {
                Model model = ModelFactory.createDefaultModel();
                model.read(new StringReader(ontologyContent), null, "TTL");
                System.out.println("✅ Ontología cargada desde almacenamiento");
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
            System.out.println("✅ Ontología guardada en almacenamiento");
            
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

    public List<Reunion> getAllReunionesSPARQL() {
        List<Reunion> reuniones = new ArrayList<>();
        Model model = loadModel();

        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema/>
            SELECT ?id ?descripcion ?lugar
            WHERE {
              ?reunion a :Reunion .
              ?reunion :ReunionID ?id .
              ?reunion :ReunionDescripcion ?descripcion .
              ?reunion :ReunionLugar ?lugar .
            }
        """;

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Reunion reunion = new Reunion();

                reunion.setReunionID(Integer.parseInt(sol.getLiteral("id").getString()));
                reunion.setReunionDescripcion(sol.getLiteral("descripcion").getString());
                reunion.setReunionLugar(sol.getLiteral("lugar").getString());

                reuniones.add(reunion);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en consulta SPARQL: " + e.getMessage());
            e.printStackTrace();
        }
        return reuniones;
    }

    public void addReunionSPARQL(Reunion reunion) throws IOException {
        Model model = loadModel();

        // Valores para Acta y Asistencia (se crean automáticamente con la reunión)
        String actaDesicion = "Acta de la reunión - Pendiente de llenar";
        String asistenciaParticipantes = "Lista de participantes - Pendiente de confirmación";

        // Insertar Reunión con relaciones a Acta y Asistencia
        String updateQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
            INSERT DATA {
              :Reunion%d a :Reunion ;
                  :ReunionID "%d"^^xsd:int ;
                  :ReunionDescripcion "%s" ;
                  :ReunionLugar "%s" ;
                  :reunionTieneActa :Acta%d ;
                  :reunionTieneAsistencia :Asistencia%d .
              
              :Acta%d a :Acta ;
                  :ActaID "%d"^^xsd:int ;
                  :ActaDesicion "%s" .
              
              :Asistencia%d a :Asistencia ;
                  :AsistenciaID "%d"^^xsd:int ;
                  :AsistenciaParticipantes "%s" .
            }
        """.formatted(
            reunion.getReunionID(),
            reunion.getReunionID(),
            escapeString(reunion.getReunionDescripcion()),
            escapeString(reunion.getReunionLugar()),
            reunion.getReunionID(),
            reunion.getReunionID(),
            reunion.getReunionID(),
            reunion.getReunionID(),
            escapeString(actaDesicion),
            reunion.getReunionID(),
            reunion.getReunionID(),
            escapeString(asistenciaParticipantes)
        );

        UpdateAction.parseExecute(updateQuery, model);
        saveModel(model);
    }

    public boolean deleteReunionSPARQL(long id) throws IOException {
        Model model = loadModel();

        // Eliminar también el Acta y Asistencia asociados
        String deleteQuery = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE WHERE {
              :Reunion%d ?p ?o .
              :Acta%d ?p2 ?o2 .
              :Asistencia%d ?p3 ?o3 .
            }
        """.formatted(id, id, id);

        UpdateAction.parseExecute(deleteQuery, model);
        saveModel(model);
        return true;
    }

    public Reunion getReunionPorIdSPARQL(long id) {
        Model model = loadModel();
        String queryString = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            SELECT ?descripcion ?lugar
            WHERE {
              ?reunion a :Reunion .
              ?reunion :ReunionID ?id .
              ?reunion :ReunionDescripcion ?descripcion .
              ?reunion :ReunionLugar ?lugar .
              FILTER(?id = %d)
            }
        """.formatted(id);

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution sol = results.nextSolution();
                Reunion reunion = new Reunion();
                reunion.setReunionID((int) id);
                reunion.setReunionDescripcion(sol.getLiteral("descripcion").getString());
                reunion.setReunionLugar(sol.getLiteral("lugar").getString());
                return reunion;
            }
        } catch (Exception e) {
            System.err.println("❌ Error consultando reunión por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateReunionSPARQL(long id, Reunion reunion) throws IOException {
        Model model = loadModel();

        String deleteOldValues = """
            PREFIX : <http://www.semanticweb.org/sebastiandiaz/ontologies/2025/9/untitled-ontology-13/>
            DELETE {
              :Reunion%d :ReunionDescripcion ?d ;
                          :ReunionLugar ?l .
            }
            INSERT {
              :Reunion%d :ReunionDescripcion "%s" ;
                          :ReunionLugar "%s" .
            }
            WHERE {
              OPTIONAL { :Reunion%d :ReunionDescripcion ?d }
              OPTIONAL { :Reunion%d :ReunionLugar ?l }
            }
        """.formatted(id, id, escapeString(reunion.getReunionDescripcion()), 
                      escapeString(reunion.getReunionLugar()), id, id);

        UpdateAction.parseExecute(deleteOldValues, model);
        saveModel(model);
        return true;
    }

    /**
     * Confirma asistencia de un residente a una reunión usando su cédula
     * Valida que la cédula exista y que no se haya confirmado más de una vez
     */
    public String confirmarAsistenciaPorCedula(Integer reunionId, Integer cedula) {
        try {
            // 🔹 1. Validar que la reunión existe (en OWL)
            Reunion reunion = getReunionPorIdSPARQL(reunionId);
            if (reunion == null) {
                return "❌ La reunión no existe";
            }
    
            // 🔹 2. Validar que el usuario existe en la base de datos
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(cedula);
            if (usuarioOpt.isEmpty()) {
                return "❌ La cédula no está registrada en el sistema";
            }
    
            Usuario usuario = usuarioOpt.get();
    
            // 🔹 3. Validar que el usuario es residente
            if (!"residente".equalsIgnoreCase(usuario.getRol())) {
                return "❌ Solo los residentes pueden confirmar asistencia";
            }
    
            // 🔹 4. Validar que no haya confirmado más de una vez
            boolean yaConfirmo = asistenciaReunionRepository.existsByReunionIdAndCedula(reunionId, cedula);
            if (yaConfirmo) {
                return "❌ Ya has confirmado tu asistencia a esta reunión";
            }
    
            // 🔹 5. Registrar asistencia en MySQL
            AsistenciaReunion asistencia = new AsistenciaReunion();
            asistencia.setReunionId(reunionId);
            asistencia.setCedula(cedula);
            asistencia.setFechaConfirmacion(LocalDateTime.now());
            asistenciaReunionRepository.save(asistencia);
    
            // 🔹 6. Actualizar en OWL (añadir nombre a lista de confirmados)
            try {
                actualizarAsistenciaOWL(reunionId, usuario.getNombre());
            } catch (Exception e) {
                System.err.println("⚠️ Error al actualizar OWL: " + e.getMessage());
            }
    
            // 🔹 7. Actualizar Acta con la lista de no confirmados
            try {
                List<Usuario> noConfirmados = getUsuariosNoConfirmados(reunionId);
                String nombresNoConfirmados = noConfirmados.stream()
                        .map(Usuario::getNombre)
                        .reduce("", (acc, n) -> acc.isEmpty() ? n : acc + ", " + n);
    
                Acta acta = new Acta();
                acta.setActaDesicion(
                    nombresNoConfirmados.isBlank()
                        ? "Todos confirmaron"
                        : "No confirmados: " + nombresNoConfirmados
                );
                actaService.updateActaSPARQL(reunionId, acta);
    
            } catch (Exception e) {
                System.err.println("⚠️ Error actualizando Acta con no confirmados: " + e.getMessage());
            }
    
            // 🔹 8. Confirmación exitosa
            return "✅ Asistencia confirmada correctamente";
    
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error al confirmar asistencia: " + e.getMessage();
        }
    }
    

    /**
     * Actualiza la lista de asistencia en OWL con el nombre del usuario
     */
    private void actualizarAsistenciaOWL(Integer reunionId, String nombreUsuario) {
        try {
            Asistencia asistencia = asistenciaService.getAsistenciaPorIdSPARQL(reunionId);
            if (asistencia != null) {
                String participantesActuales = asistencia.getAsistenciaParticipantes();
                String nuevoParticipante;
                
                if (participantesActuales == null || participantesActuales.trim().isEmpty() || 
                    participantesActuales.equals("Lista de participantes - Pendiente de confirmación")) {
                    nuevoParticipante = nombreUsuario;
                } else {
                    nuevoParticipante = participantesActuales + ", " + nombreUsuario;
                }

                asistencia.setAsistenciaParticipantes(nuevoParticipante);
                asistenciaService.updateAsistenciaSPARQL(reunionId, asistencia);
            }
        } catch (Exception e) {
            // Si falla la actualización en OWL, no es crítico ya que la confirmación está en MySQL
            System.err.println("Error al actualizar OWL: " + e.getMessage());
        }
    }

    /**
     * Obtiene la lista de usuarios que han confirmado asistencia a una reunión
     */
    public List<Usuario> getUsuariosConfirmados(Integer reunionId) {
        return asistenciaReunionRepository.findUsuariosConfirmados(reunionId);
    }

    /**
     * Obtiene la lista de usuarios que NO han confirmado asistencia a una reunión
     */
    public List<Usuario> getUsuariosNoConfirmados(Integer reunionId) {
        return asistenciaReunionRepository.findUsuariosNoConfirmados(reunionId);
    }

    /**
     * Obtiene todas las confirmaciones de una reunión con detalles
     */
    public List<AsistenciaReunion> getConfirmacionesReunion(Integer reunionId) {
        return asistenciaReunionRepository.findByReunionId(reunionId);
    }
}