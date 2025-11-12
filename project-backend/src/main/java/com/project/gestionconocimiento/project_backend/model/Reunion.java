package com.project.gestionconocimiento.project_backend.model;

public class Reunion {
    private int reunionID;
    private String reunionDescripcion;
    private String reunionLugar;

    public int getReunionID() {
        return reunionID;
    }

    public void setReunionID(int id) {
        this.reunionID = id;
    }

    public String getReunionDescripcion() {
        return reunionDescripcion;
    }

    public void setReunionDescripcion(String descripcion) {
        this.reunionDescripcion = descripcion;
    }

    public String getReunionLugar() {
        return reunionLugar;
    }

    public void setReunionLugar(String lugar) {
        this.reunionLugar = lugar;
    }
}

