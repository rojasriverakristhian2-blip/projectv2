import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { interAsistencia } from '../../../../Service/Asistencia.service';

@Component({
  selector: 'app-view-asistencia',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-asistencia.html',
  styleUrl: './view-asistencia.css',
})
export class ViewAsistencia implements OnInit {
  asistencia!: interAsistencia;

  constructor(
    private dialogRef: MatDialogRef<ViewAsistencia>,
    @Inject(MAT_DIALOG_DATA) public data: interAsistencia
  ) {}

  ngOnInit(): void {
    this.asistencia = { ...this.data };
  }

  cerrar() {
    this.dialogRef.close();
  }
}

