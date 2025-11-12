import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddReunion } from './add-reunion/add-reunion';
import { ViewReunion } from './view-reunion/view-reunion';
import { ListaAsistencias } from './lista-asistencias/lista-asistencias';
import { ReunionService, interReunion } from '../../Service/Reunion.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reunion',
  templateUrl: './reunion.html',
  styleUrl: './reunion.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class Reunion implements OnInit {
  reuniones: interReunion[] = [];
  codigoBusqueda: string = '';

  constructor(
    private dialog: MatDialog,
    private reunionServicio: ReunionService
  ) { }

  ngOnInit(): void {
    this.cargarReuniones();
  }

  cargarReuniones() {
    this.reunionServicio.getReuniones().subscribe({
      next: (data) => {
        this.reuniones = data;
      },
      error: (error) => {
        console.error('Error al cargar reuniones:', error);
      }
    });
  }

  AddReunion() {
    const dialogRef = this.dialog.open(AddReunion, {
      width: '1200px',
      height: 'auto',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarReuniones();
      }
    });
  }

  ViewReunion(reunion: interReunion) {
    this.dialog.open(ViewReunion, {
      width: '1200px',
      height: 'auto',
      data: reunion
    });
  }

  VerListaAsistencias(reunion: interReunion) {
    this.dialog.open(ListaAsistencias, {
      width: '1200px',
      height: 'auto',
      data: reunion
    });
  }

  deleteReunion(reunion: interReunion) {
    if (confirm(`¿Estás seguro de eliminar la reunión con ID "${reunion.reunionID}"?`)) {
      this.reunionServicio.deleteReunion(reunion.reunionID).subscribe({
        next: () => {
          alert('✅ Reunión eliminada correctamente');
          this.cargarReuniones();
        },
        error: (err) => {
          console.error('Error al eliminar la reunión:', err);
          alert('❌ Error al eliminar la reunión');
        }
      });
    }
    
  }

  buscarPorCodigo() {
    const id = Number(this.codigoBusqueda.trim());
    if (!id) {
      this.cargarReuniones();
      return;
    }
    
    this.reunionServicio.getReunionById(id).subscribe({
      next: (reunion) => {
        if (reunion) {
          this.reuniones = [reunion];
        } else {
          alert('⚠️ No se encontró ninguna reunión con ese código.');
          this.reuniones = [];
        }
      },
      error: (err) => {
        console.error('Error al buscar reunión:', err);
        alert('❌ No se encontró la reunión.');
      }
    });
  }
}

