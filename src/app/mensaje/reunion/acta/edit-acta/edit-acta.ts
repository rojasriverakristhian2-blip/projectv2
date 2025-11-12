import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActaService, interActa } from '../../../../Service/Acta.service';

@Component({
  selector: 'app-edit-acta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-acta.html',
  styleUrl: './edit-acta.css',
})
export class EditActa implements OnInit {
  acta: interActa = {
    actaID: 0,
    actaDesicion: ''
  };

  constructor(
    private actaService: ActaService,
    private dialogRef: MatDialogRef<EditActa>,
    @Inject(MAT_DIALOG_DATA) public data: interActa
  ) {}

  ngOnInit(): void {
    this.acta = { ...this.data };
  }

  guardarCambios() {
    this.actaService.updateActa(this.acta.actaID, this.acta).subscribe({
      next: () => {
        alert('✅ Acta actualizada correctamente');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error al actualizar acta:', err);
        alert('❌ Error al guardar los cambios.');
      },
    });
  }

  cancelar() {
    this.dialogRef.close(false);
  }
}

