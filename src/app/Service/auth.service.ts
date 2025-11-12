import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.prod';    
export interface Usuario {
  correo: string;
  contrasena: string;
  rol: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // SIN /auth al final - Angular agregará el endpoint completo
  private baseUrl = `${environment.apiUrl}/api/auth`;

  constructor(private http: HttpClient) {}

  login(usuario: Usuario): Observable<string> {
    return this.http.post(`${this.baseUrl}/login`, usuario, { responseType: 'text' });
  }
}
