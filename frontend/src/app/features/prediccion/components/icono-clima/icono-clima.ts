import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';

import { TipoClima } from '../../../../core/clima/tipo-clima';

const DESCRIPCIONES: Record<TipoClima, string> = {
  sol: 'Soleado',
  calor: 'Calor',
  frio: 'Frío',
  nubes: 'Nublado',
  lluvia: 'Lluvia',
  nieve: 'Nieve',
};

@Component({
  selector: 'app-icono-clima',
  templateUrl: './icono-clima.html',
  styleUrl: './icono-clima.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IconoClima {
  readonly tipo = input.required<TipoClima>();
  readonly tamano = input(48);

  protected readonly descripcion = computed(() => DESCRIPCIONES[this.tipo()]);
}
