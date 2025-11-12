import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AsistenciaService, interAsistencia } from '../../../../Service/Asistencia.service';

@Component({
  selector: 'app-add-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-asistencia.html',
  styleUrl: './add-asistencia.css',
})
export class AddAsistencia {
  nuevaAsistencia: interAsistencia = {
    asistenciaID: 0,
    asistenciaParticipantes: '',
  };

  constructor(
    private asistenciaService: AsistenciaService,
    private dialogRef: MatDialogRef<AddAsistencia>
  ) {}

  guardarAsistencia() {
    if (!this.nuevaAsistencia.asistenciaParticipantes) {
      alert('⚠️ Debes completar todos los campos obligatorios.');
      return;
    }

    this.asistenciaService.addAsistencia(this.nuevaAsistencia).subscribe({
      next: () => {
        alert('✅ Asistencia registrada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al guardar asistencia', err);
        alert('❌ Error al guardar la asistencia');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}

