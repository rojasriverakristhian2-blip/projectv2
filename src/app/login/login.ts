import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../Service/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  imports: [FormsModule, CommonModule],
})
export class Login {
  correo: string = '';
  contrasena: string = '';
  rol: string = 'residente';
  mensaje: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.mensaje = '';

    if (!this.correo || !this.contrasena) {
      this.mensaje = '⚠️ Por favor completa todos los campos.';
      return;
    }

    this.authService.login({
      correo: this.correo,
      contrasena: this.contrasena,
      rol: this.rol
    }).subscribe({
      next: (response) => {
        if (response.includes('exitoso')) {
          this.mensaje = '✅ Acceso concedido';
          setTimeout(() => {
            this.router.navigate([this.rol === 'presidente' ? '/presidente' : '/residente']);
          }, 1000);
        } else {
          this.mensaje = '❌ Acceso denegado';
        }
      },
      error: () => {
        this.mensaje = '❌ Error al conectar con el servidor.';
      }
    });
  }
}
