import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AsistenciaService, interAsistencia } from '../../../../Service/Asistencia.service';

@Component({
  selector: 'app-edit-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-asistencia.html',
  styleUrl: './edit-asistencia.css',
})
export class EditAsistencia implements OnInit {
  asistencia: interAsistencia = {
    asistenciaID: 0,
    asistenciaParticipantes: ''
  };

  constructor(
    private asistenciaService: AsistenciaService,
    private dialogRef: MatDialogRef<EditAsistencia>,
    @Inject(MAT_DIALOG_DATA) public data: interAsistencia
  ) {}

  ngOnInit(): void {
    this.asistencia = { ...this.data };
  }

  guardarCambios() {
    this.asistenciaService.updateAsistencia(this.asistencia.asistenciaID, this.asistencia).subscribe({
      next: () => {
        alert('✅ Asistencia actualizada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al actualizar asistencia:', err);
        alert('❌ Error al guardar los cambios.');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}

