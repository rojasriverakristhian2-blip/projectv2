import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActividadService, interActividad } from '../../../Service/actividad.service';

@Component({
  selector: 'app-add-actividad',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-actividad.html',
  styleUrl: './add-actividad.css',
})
export class AddActividad {
  nuevaActividad: interActividad = {
    actividadID: 0,
    actividadNombre: '',
    actividadFecha: '',
    actividadDescripcion: '',
  };

  constructor(
    private actividadService: ActividadService,
    private dialogRef: MatDialogRef<AddActividad>
  ) {}

  guardarActividad() {
    if (
      !this.nuevaActividad.actividadNombre ||
      !this.nuevaActividad.actividadFecha ||
      !this.nuevaActividad.actividadDescripcion
    ) {
      alert('⚠️ Debes completar todos los campos obligatorios.');
      return;
    }

    this.actividadService.addActividad(this.nuevaActividad).subscribe({
      next: () => {
        alert('✅ Actividad registrada correctamente');
        this.dialogRef.close(true); 
      },
      error: (err) => {
        console.error('Error al guardar actividad', err);
        alert('❌ Error al guardar la actividad');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}
