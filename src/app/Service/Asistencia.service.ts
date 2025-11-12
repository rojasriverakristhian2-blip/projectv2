import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.prod';
export interface interAsistencia {
  asistenciaID: number;
  asistenciaParticipantes: string;
}

@Injectable({ providedIn: 'root' })
export class AsistenciaService {
  private apiUrl = `${environment.apiUrl}/api/asistencias`; // ← Usando variable de entorno para el backend

  constructor(private http: HttpClient) { }

  getAsistencias(): Observable<interAsistencia[]> {
    return this.http.get<interAsistencia[]>(this.apiUrl);
  }

  addAsistencia(asistencia: interAsistencia): Observable<any> {
    return this.http.post<any>(this.apiUrl, asistencia);
  }

  deleteAsistencia(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getAsistenciaById(id: number): Observable<interAsistencia> {
    return this.http.get<interAsistencia>(`${this.apiUrl}/${id}`);
  }

  updateAsistencia(id: number, asistencia: interAsistencia): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, asistencia, { responseType: 'text' });
  }
}

