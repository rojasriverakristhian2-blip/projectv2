import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddActa } from './add-acta/add-acta';
import { EditActa } from './edit-acta/edit-acta';
import { ViewActa } from './view-acta/view-acta';
import { ActaService, interActa } from '../../../Service/Acta.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-acta',
  templateUrl: './acta.html',
  styleUrl: './acta.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class Acta implements OnInit {
  actas: interActa[] = [];
  codigoBusqueda: string = '';

  constructor(
    private dialog: MatDialog,
    private actaServicio: ActaService
  ) { }

  ngOnInit(): void {
    this.cargarActas();
  }

  cargarActas() {
    this.actaServicio.getActas().subscribe({
      next: (data) => {
        this.actas = data;
      },
      error: (error) => {
        console.error('Error al cargar actas:', error);
      }
    });
  }

  AddActa() {
    const dialogRef = this.dialog.open(AddActa, {
      width: '1200px',
      height: 'auto',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarActas();
      }
    });
  }

  EditActa(acta: interActa) {
    const dialogRef = this.dialog.open(EditActa, {
      width: '1200px',
      height: 'auto',
      data: acta
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarActas();
      }
    });
  }

  ViewActa(acta: interActa) {
    this.dialog.open(ViewActa, {
      width: '1200px',
      height: 'auto',
      data: acta
    });
  }

  deleteActa(acta: interActa) {
    if (confirm(`¿Estás seguro de eliminar el acta con ID "${acta.actaID}"?`)) {
      this.actaServicio.deleteActa(acta.actaID).subscribe({
        next: () => {
          alert('✅ Acta eliminada correctamente');
          this.cargarActas();
        },
        error: (err) => {
          console.error('Error al eliminar el acta:', err);
          alert('❌ Error al eliminar el acta');
        }
      });
    }
  }

  buscarPorCodigo() {
    const id = Number(this.codigoBusqueda.trim());
    if (!id) {
      this.cargarActas();
      return;
    }

    this.actaServicio.getActaById(id).subscribe({
      next: (acta) => {
        if (acta) {
          this.actas = [acta];
        } else {
          alert('⚠️ No se encontró ningún acta con ese código.');
          this.actas = [];
        }
      },
      error: (err) => {
        console.error('Error al buscar acta:', err);
        alert('❌ No se encontró el acta.');
      }
    });
  }
}
