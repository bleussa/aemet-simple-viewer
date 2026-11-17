import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

import { consejoAbrigo } from '../../../../core/clima/consejo-abrigo';
import { Municipio } from '../../../../core/models/municipio';
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
  readonly municipio = input.required<Municipio>();
  readonly prediccion = input.required<Prediccion>();

  protected readonly consejo = computed(() =>
    consejoAbrigo(this.prediccion(), this.municipio().codigo),
  );
  protected readonly simbolo = computed(() => SIMBOLOS[this.prediccion().unidadTemperatura]);
}
