import { ChangeDetectionStrategy, Component, model } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { UnidadTemperatura } from '../../../../core/models/prediccion';

@Component({
  selector: 'app-selector-unidad',
  imports: [MatFormFieldModule, MatSelectModule],
  templateUrl: './selector-unidad.html',
  styleUrl: './selector-unidad.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SelectorUnidad {
  readonly unidad = model<UnidadTemperatura | undefined>();
}
