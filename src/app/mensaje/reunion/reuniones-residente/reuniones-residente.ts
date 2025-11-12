import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmarAsistencia } from '../confirmar-asistencia/confirmar-asistencia';
import { ReunionService, interReunion } from '../../../Service/Reunion.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reuniones-residente',
  templateUrl: './reuniones-residente.html',
  styleUrl: './reuniones-residente.css',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ReunionesResidente implements OnInit {
  reuniones: interReunion[] = [];

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

  confirmarAsistencia(reunion: interReunion) {
    const dialogRef = this.dialog.open(ConfirmarAsistencia, {
      width: '800px',
      height: 'auto',
      data: reunion
    });

    dialogRef.afterClosed().subscribe(result => {
      // No necesitamos recargar, pero podríamos mostrar un mensaje
    });
  }
}

