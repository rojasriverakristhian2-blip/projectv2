import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReunionService, interReunion, Usuario } from '../../../Service/Reunion.service';

@Component({
  selector: 'app-lista-asistencias',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-asistencias.html',
  styleUrl: './lista-asistencias.css',
})
export class ListaAsistencias implements OnInit {
  reunion!: interReunion;
  usuariosConfirmados: Usuario[] = [];
  usuariosNoConfirmados: Usuario[] = [];
  loading: boolean = true;
  activeTab: 'confirmados' | 'noConfirmados' = 'confirmados';

  constructor(
    private reunionService: ReunionService,
    private dialogRef: MatDialogRef<ListaAsistencias>,
    @Inject(MAT_DIALOG_DATA) public data: interReunion
  ) {}

  ngOnInit(): void {
    this.reunion = { ...this.data };
    this.cargarListas();
  }

  cargarListas() {
    this.loading = true;
    
    // Cargar confirmados
    this.reunionService.getUsuariosConfirmados(this.reunion.reunionID).subscribe({
      next: (data) => {
        this.usuariosConfirmados = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar confirmados:', error);
        this.loading = false;
      }
    });

    // Cargar no confirmados
    this.reunionService.getUsuariosNoConfirmados(this.reunion.reunionID).subscribe({
      next: (data) => {
        this.usuariosNoConfirmados = data;
      },
      error: (error) => {
        console.error('Error al cargar no confirmados:', error);
      }
    });
  }

  cambiarTab(tab: 'confirmados' | 'noConfirmados') {
    this.activeTab = tab;
  }

  cerrar() {
    this.dialogRef.close();
  }
}

