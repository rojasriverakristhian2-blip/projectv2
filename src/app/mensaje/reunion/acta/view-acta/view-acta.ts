import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { interActa } from '../../../../Service/Acta.service';

@Component({
  selector: 'app-view-acta',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-acta.html',
  styleUrl: './view-acta.css',
})
export class ViewActa implements OnInit {
  acta!: interActa;

  constructor(
    private dialogRef: MatDialogRef<ViewActa>,
    @Inject(MAT_DIALOG_DATA) public data: interActa
  ) {}

  ngOnInit(): void {
    this.acta = { ...this.data };
  }

  cerrar() {
    this.dialogRef.close();
  }
}

