import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.prod';
export interface interActividad {
  actividadID: number;
  actividadNombre: string;
  actividadFecha: string;
  actividadDescripcion: string;
}

@Injectable({ providedIn: 'root' })
export class ActividadService {
  private apiUrl = `${environment.apiUrl}/api/actividades`; // ← Usando variable de entorno para el backend

  constructor(private http: HttpClient) { }

  getActividades(): Observable<interActividad[]> {
    return this.http.get<interActividad[]>(this.apiUrl);
  }

  addActividad(actividad: interActividad): Observable<any> {
    return this.http.post<any>(this.apiUrl, actividad);
  }

  deleteActividad(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getActividadById(id: number): Observable<interActividad> {
    return this.http.get<interActividad>(`${this.apiUrl}/${id}`);
  }

  updateActividad(id: number, actividad: interActividad): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, actividad, { responseType: 'text' });
  }
  


 
}
