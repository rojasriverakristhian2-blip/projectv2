import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ReunionService, interReunion } from '../../../Service/Reunion.service';

@Component({
  selector: 'app-add-reunion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-reunion.html',
  styleUrl: './add-reunion.css',
})
export class AddReunion {
  nuevaReunion: interReunion = {
    reunionID: 0,
    reunionDescripcion: '',
    reunionLugar: '',
  };

  constructor(
    private reunionService: ReunionService,
    private dialogRef: MatDialogRef<AddReunion>
  ) {}

  guardarReunion() {
    if (!this.nuevaReunion.reunionDescripcion || !this.nuevaReunion.reunionLugar) {
      alert('⚠️ Debes completar todos los campos obligatorios.');
      return;
    }

    this.reunionService.addReunion(this.nuevaReunion).subscribe({
      next: () => {
        alert('✅ Reunión creada correctamente. Se han creado automáticamente el Acta y la Asistencia asociadas.');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al guardar reunión', err);
        alert('❌ Error al guardar la reunión');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}

