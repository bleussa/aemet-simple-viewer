import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

import { Prediccion } from '../../../../core/models/prediccion';

const SIMBOLOS = { G_CEL: '°C', G_FAH: '°F' } as const;

@Component({
  selector: 'app-prediccion-resultado',
  imports: [DecimalPipe, MatCardModule],
  templateUrl: './prediccion-resultado.html',
  styleUrl: './prediccion-resultado.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PrediccionResultado {
  readonly nombreMunicipio = input.required<string>();
  readonly prediccion = input.required<Prediccion>();

  protected readonly simbolo = computed(() => SIMBOLOS[this.prediccion().unidadTemperatura]);
}
