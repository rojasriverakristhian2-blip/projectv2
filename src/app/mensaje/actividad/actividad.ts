import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddActividad } from './add-actividad/add-actividad';
import { EditActividad } from './edit-actividad/edit-actividad';
import { ViewActividad } from './view-actividad/view-actividad';
import { ActividadService, interActividad } from '../../Service/actividad.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-actividad',
  templateUrl: './actividad.html',
  styleUrl: './actividad.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class Actividad implements OnInit {
  actividades: interActividad[] = [];
  codigoBusqueda: string = '';

  constructor(
    private dialog: MatDialog,
    private actividadServicio: ActividadService
  ) { }

  ngOnInit(): void {
    this.cargarActividades();
  }

  cargarActividades() {
    this.actividadServicio.getActividades().subscribe({
      next: (data) => {
        this.actividades = data;
      },
      error: (error) => {
        console.error('Error al cargar actividades:', error);
      }
    });
  }



  AddActividad() {
    const dialogRef = this.dialog.open(AddActividad, {
      width: '1200px',     // tamaño de la ventana
      height: 'auto',
    });

    // Actualizar la tabla cuando se cierra el diálogo
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarActividades(); // Recargar las actividades
      }
    });
  }

  EditActividad(actividad: interActividad) {
    const dialogRef = this.dialog.open(EditActividad, {
      width: '1200px',
      height: 'auto',
      data: actividad // Pasar los datos de la actividad a editar
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.cargarActividades();
      }
    });
  }

  ViewActividad(actividad: interActividad) {
    this.dialog.open(ViewActividad, {
      width: '1200px',
      height: 'auto',
      data: actividad
    });
  }

  deleteActividad(actividad: interActividad) {
    if (confirm(`¿Estás seguro de eliminar la actividad "${actividad.actividadNombre}"?`)) {
      this.actividadServicio.deleteActividad(actividad.actividadID).subscribe({
        next: () => {
          alert('✅ Actividad eliminada correctamente');
          this.cargarActividades(); // Recarga la lista
        },
        error: (err) => {
          console.error('Error al eliminar la actividad:', err);
          alert('❌ Error al eliminar la actividad');
        }
      });
    }
  }

  buscarPorCodigo() {
    const id = Number(this.codigoBusqueda.trim());
    if (!id) {
      this.cargarActividades(); // Si el campo está vacío, recarga todo
      return;
    }

    this.actividadServicio.getActividadById(id).subscribe({
      next: (act) => {
        if (act) {
          this.actividades = [act]; // 👈 muestra solo la actividad encontrada
        } else {
          alert('⚠️ No se encontró ninguna actividad con ese código.');
          this.actividades = [];
        }
      },
      error: (err) => {
        console.error('Error al buscar actividad:', err);
        alert('❌ No se encontró la actividad.');
      }
    });
  
  }
  
}