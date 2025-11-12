import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { interActividad } from '../../../Service/actividad.service';

@Component({
  selector: 'app-view-actividad',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-actividad.html',
  styleUrl: './view-actividad.css',
})
export class ViewActividad implements OnInit {
  actividad!: interActividad;

  constructor(
    private dialogRef: MatDialogRef<ViewActividad>,
    @Inject(MAT_DIALOG_DATA) public data: interActividad
  ) {}

  ngOnInit(): void {
    // Carga los datos de la actividad recibidos desde el componente principal
    this.actividad = { ...this.data };
  }

  cerrar() {
    this.dialogRef.close();
  }
}
