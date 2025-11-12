import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-informacion',
  imports: [],
  templateUrl: './informacion.html',
  styleUrl: './informacion.css',
})
export class Informacion {
  textoLetrero: string = "¡Bienvenido! • Este es un letrero informativo • ";

  get TextoLetrero(): string {
    return this.textoLetrero;
  }

  set TextoLetrero(valor: string) {
    this.textoLetrero = valor;
  }
}

 


