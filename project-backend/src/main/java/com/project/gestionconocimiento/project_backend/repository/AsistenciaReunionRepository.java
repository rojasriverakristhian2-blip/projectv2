package com.project.gestionconocimiento.project_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.gestionconocimiento.project_backend.model.AsistenciaReunion;
import com.project.gestionconocimiento.project_backend.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaReunionRepository extends JpaRepository<AsistenciaReunion, Long> {
    
    // ✅ Nuevo método para verificar si ya existe la confirmación
    boolean existsByReunionIdAndCedula(Integer reunionId, Integer cedula);
    
    // Verificar si una cédula ya confirmó asistencia a una reunión
    Optional<AsistenciaReunion> findByReunionIdAndCedula(Integer reunionId, Integer cedula);
    
    // Contar cuántas veces una cédula ha confirmado en una reunión
    long countByReunionIdAndCedula(Integer reunionId, Integer cedula);
    
    // Obtener todas las confirmaciones de una reunión
    List<AsistenciaReunion> findByReunionId(Integer reunionId);
    
    // Obtener usuarios que confirmaron asistencia a una reunión
    @Query("SELECT u FROM Usuario u INNER JOIN AsistenciaReunion ar ON u.cedula = ar.cedula WHERE ar.reunionId = :reunionId")
    List<Usuario> findUsuariosConfirmados(@Param("reunionId") Integer reunionId);
    
    // Obtener usuarios que NO confirmaron asistencia (residentes)
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'residente' AND " +
           "(SELECT COUNT(ar) FROM AsistenciaReunion ar WHERE ar.reunionId = :reunionId AND ar.cedula = u.cedula) = 0")
    List<Usuario> findUsuariosNoConfirmados(@Param("reunionId") Integer reunionId);
}
