package com.project.gestionconocimiento.project_backend.model;

public class Actividad {
    private int actividadID;
    private String actividadNombre;
    private String actividadFecha;
    private String actividadDescripcion;

    public int getActividadID() {
        return actividadID;
    }

    public void setActividadID(int id) {
        this.actividadID = id;
    }

    public String getActividadNombre() {
        return actividadNombre;
    }

    public void setActividadNombre(String nombre) {
        this.actividadNombre = nombre;
    }

    public String getActividadFecha() {
        return actividadFecha;
    }

    public void setActividadFecha(String fecha) {
        this.actividadFecha = fecha;
    }

    public String getActividadDescripcion() {
        return actividadDescripcion;
    }

    public void setActividadDescripcion(String descripcion) {
        this.actividadDescripcion = descripcion;
    }
}
