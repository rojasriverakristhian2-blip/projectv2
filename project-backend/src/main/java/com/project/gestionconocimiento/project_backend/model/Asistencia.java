package com.project.gestionconocimiento.project_backend.model;

public class Asistencia {
    private int asistenciaID;
    private String asistenciaParticipantes;

    public int getAsistenciaID() {
        return asistenciaID;
    }

    public void setAsistenciaID(int id) {
        this.asistenciaID = id;
    }

    public String getAsistenciaParticipantes() {
        return asistenciaParticipantes;
    }

    public void setAsistenciaParticipantes(String participantes) {
        this.asistenciaParticipantes = participantes;
    }
}

