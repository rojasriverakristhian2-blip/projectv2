import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddAsistencia } from './add-asistencia/add-asistencia';
import { EditAsistencia } from './edit-asistencia/edit-asistencia';
import { ViewAsistencia } from './view-asistencia/view-asistencia';
import { AsistenciaService, interAsistencia } from '../../../Service/Asistencia.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-asistencia',
  templateUrl: './asistencia.html',
  styleUrl: './asistencia.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class Asistencia implements OnInit {
  asistencias: interAsistencia[] = [];
  codigoBusqueda: string = '';

  constructor(
    private dialog: MatDialog,
    private asistenciaServicio: AsistenciaService
  ) { }

  ngOnInit(): void {
    this.cargarAsistencias();
  }

  cargarAsistencias() {
    this.asistenciaServicio.getAsistencias().subscribe({
      next: (data) => {
        this.asistencias = data;
      },
      error: (error) => {
        console.error('Error al cargar asistencias:', error);
      }
    });
  }

  AddAsistencia() {
    const dialogRef = this.dialog.open(AddAsistencia, {
      width: '1200px',
      height: 'auto',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarAsistencias();
      }
    });
  }

  EditAsistencia(asistencia: interAsistencia) {
    const dialogRef = this.dialog.open(EditAsistencia, {
      width: '1200px',
      height: 'auto',
      data: asistencia
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarAsistencias();
      }
    });
  }

  ViewAsistencia(asistencia: interAsistencia) {
    this.dialog.open(ViewAsistencia, {
      width: '1200px',
      height: 'auto',
      data: asistencia
    });
  }

  deleteAsistencia(asistencia: interAsistencia) {
    if (confirm(`¿Estás seguro de eliminar la asistencia con ID "${asistencia.asistenciaID}"?`)) {
      this.asistenciaServicio.deleteAsistencia(asistencia.asistenciaID).subscribe({
        next: () => {
          alert('✅ Asistencia eliminada correctamente');
          this.cargarAsistencias();
        },
        error: (err) => {
          console.error('Error al eliminar la asistencia:', err);
          alert('❌ Error al eliminar la asistencia');
        }
      });
    }
  }

  buscarPorCodigo() {
    const id = Number(this.codigoBusqueda.trim());
    if (!id) {
      this.cargarAsistencias();
      return;
    }

    this.asistenciaServicio.getAsistenciaById(id).subscribe({
      next: (asistencia) => {
        if (asistencia) {
          this.asistencias = [asistencia];
        } else {
          alert('⚠️ No se encontró ninguna asistencia con ese código.');
          this.asistencias = [];
        }
      },
      error: (err) => {
        console.error('Error al buscar asistencia:', err);
        alert('❌ No se encontró la asistencia.');
      }
    });
  }
}
