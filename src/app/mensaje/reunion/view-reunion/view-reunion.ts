import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { interReunion } from '../../../Service/Reunion.service';

@Component({
  selector: 'app-view-reunion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './view-reunion.html',
  styleUrl: './view-reunion.css',
})
export class ViewReunion implements OnInit {
  reunion!: interReunion;

  constructor(
    private dialogRef: MatDialogRef<ViewReunion>,
    @Inject(MAT_DIALOG_DATA) public data: interReunion
  ) {}

  ngOnInit(): void {
    this.reunion = { ...this.data };
  }

  cerrar() {
    this.dialogRef.close();
  }
}

