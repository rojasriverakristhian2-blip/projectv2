import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Actividad } from '../../mensaje/actividad/actividad';
import { Reunion } from '../../mensaje/reunion/reunion';

@Component({
  selector: 'app-presidente',
  imports: [],
  templateUrl: './presidente.html',
  styleUrl: './presidente.css',
})
export class Presidente {

  constructor(private dialog: MatDialog) {
  }


  flotanteActividad() {
    this.dialog.open(Actividad, {
      width: '1200',     // tamaño de la ventana
      height: 'auto',
      data: { nombre: 'Presidente del conjunto' }  // datos opcionales
    });
  }

  flotanteReunion() {
    this.dialog.open(Reunion, {
      width: '1200px',
      height: 'auto',
    });
  }
}
