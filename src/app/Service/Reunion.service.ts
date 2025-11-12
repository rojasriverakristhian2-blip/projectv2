import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.prod';  
export interface interReunion {
  reunionID: number;
  reunionDescripcion: string;
  reunionLugar: string;
}

@Injectable({ providedIn: 'root' })
export class ReunionService {
  private apiUrl = `${environment.apiUrl}/reuniones`; // ← Usando variable de entorno para el backend

  constructor(private http: HttpClient) { }

  getReuniones(): Observable<interReunion[]> {
    return this.http.get<interReunion[]>(this.apiUrl);
  }

  addReunion(reunion: interReunion): Observable<any> {
    return this.http.post<any>(this.apiUrl, reunion);
  }

  deleteReunion(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getReunionById(id: number): Observable<interReunion> {
    return this.http.get<interReunion>(`${this.apiUrl}/${id}`);
  }

  updateReunion(id: number, reunion: interReunion): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, reunion, { responseType: 'text' });
  }

  confirmarAsistencia(reunionId: number, cedula: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${reunionId}/confirmar-asistencia`, 
      { cedula }, 
      { responseType: 'text' });
  }

  getUsuariosConfirmados(reunionId: number): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/${reunionId}/confirmados`);
  }

  getUsuariosNoConfirmados(reunionId: number): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/${reunionId}/no-confirmados`);
  }
}

export interface Usuario {
  cedula: number;
  nombre: string;
  correo: string;
  telefono: string;
  rol: string;
  fechaNacimiento: string;
}

