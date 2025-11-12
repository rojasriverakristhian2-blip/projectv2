import { Component, OnInit } from '@angular/core';
import { ActividadService, interActividad } from '../../Service/actividad.service';
import { Informacion } from '../../mensaje/informacion/informacion';
import { MatDialog } from '@angular/material/dialog';
import { ReunionesResidente } from '../../mensaje/reunion/reuniones-residente/reuniones-residente';

@Component({
  selector: 'app-residente',
  imports: [],
  templateUrl: './residente.html',
  styleUrl: './residente.css',
})
export class Residente implements OnInit {
  actividades: interActividad[] = [];
  mensajeLetrero: String = new Informacion().TextoLetrero;

  constructor(
    private actividadService: ActividadService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.cargarActividades();
  }

  cargarActividades() {
    this.actividadService.getActividades().subscribe((data) => {
      this.actividades = data;
    });
  }

  verReuniones() {
    this.dialog.open(ReunionesResidente, {
      width: '1000px',
      height: 'auto',
    });
  }
}
