import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActividadService, interActividad } from '../../../Service/actividad.service';
import { Actividad } from '../actividad';

@Component({
  selector: 'app-edit-actividad',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-actividad.html',
  styleUrl: './edit-actividad.css',
})
export class EditActividad implements OnInit {
  actividad: interActividad = {
    actividadID: 0,
    actividadNombre: '',
    actividadFecha: '',
    actividadDescripcion: ''
  };

  constructor(
    private actividadService: ActividadService,
    private dialogRef: MatDialogRef<EditActividad>,
    @Inject(MAT_DIALOG_DATA) public data: interActividad
  ) {}

  ngOnInit(): void {
    this.actividad = { ...this.data }; // 📦 Cargar los datos en el formulario
  }

  guardarCambios() {
    this.actividadService.updateActividad(this.actividad.actividadID, this.actividad).subscribe({
      next: () => {
        alert('✅ Actividad actualizada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al actualizar actividad:', err);
        alert('❌ Error al guardar los cambios.');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}
