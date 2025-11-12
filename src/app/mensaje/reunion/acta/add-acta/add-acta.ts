import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActaService, interActa } from '../../../../Service/Acta.service';

@Component({
  selector: 'app-add-acta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-acta.html',
  styleUrl: './add-acta.css',
})
export class AddActa {
  nuevaActa: interActa = {
    actaID: 0,
    actaDesicion: '',
  };

  constructor(
    private actaService: ActaService,
    private dialogRef: MatDialogRef<AddActa>
  ) {}

  guardarActa() {
    if (!this.nuevaActa.actaDesicion) {
      alert('⚠️ Debes completar todos los campos obligatorios.');
      return;
    }

    this.actaService.addActa(this.nuevaActa).subscribe({
      next: () => {
        alert('✅ Acta registrada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al guardar acta', err);
        alert('❌ Error al guardar el acta');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}

