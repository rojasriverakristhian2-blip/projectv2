import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.prod';


export interface interActa {
  actaID: number;
  actaDesicion: string;
}

@Injectable({ providedIn: 'root' })
export class ActaService {
  private apiUrl = `${environment.apiUrl}/api/actas`; // ← Usando variable de entorno para el backend

  constructor(private http: HttpClient) { }

  getActas(): Observable<interActa[]> {
    return this.http.get<interActa[]>(this.apiUrl);
  }

  addActa(acta: interActa): Observable<any> {
    return this.http.post<any>(this.apiUrl, acta);
  }

  deleteActa(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getActaById(id: number): Observable<interActa> {
    return this.http.get<interActa>(`${this.apiUrl}/${id}`);
  }

  updateActa(id: number, acta: interActa): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, acta, { responseType: 'text' });
  }
}

