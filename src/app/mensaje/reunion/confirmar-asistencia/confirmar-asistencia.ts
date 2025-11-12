import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReunionService, interReunion } from '../../../Service/Reunion.service';

@Component({
  selector: 'app-confirmar-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './confirmar-asistencia.html',
  styleUrl: './confirmar-asistencia.css',
})
export class ConfirmarAsistencia implements OnInit {
  reunion!: interReunion;
  cedula: number | null = null; // 👈 ahora es tipo número

  constructor(
    private reunionService: ReunionService,
    private dialogRef: MatDialogRef<ConfirmarAsistencia>,
    @Inject(MAT_DIALOG_DATA) public data: interReunion
  ) {}

  ngOnInit(): void {
    this.reunion = { ...this.data };
  }

  confirmar() {
    // Validar que la cédula no esté vacía
    if (this.cedula === null || this.cedula === undefined) {
      alert('⚠️ Por favor ingresa tu número de cédula');
      return;
    }

    // Validar que la cédula sea un número entero válido
    if (!Number.isInteger(this.cedula) || this.cedula <= 0) {
      alert('⚠️ La cédula debe ser un número entero válido');
      return;
    }

    // Llamar al backend
    this.reunionService.confirmarAsistencia(this.reunion.reunionID, this.cedula).subscribe({
      next: (response) => {
        console.log('Respuesta del backend:', response);
        alert(response);
        if (response.includes('✅')) {
          this.dialogRef.close(true);
        }
      },
      error: (err) => {
        console.error('Error al confirmar asistencia:', err);
        const errorMessage = err.error?.message || err.error || '❌ Error al confirmar la asistencia';
        alert(errorMessage);
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}
