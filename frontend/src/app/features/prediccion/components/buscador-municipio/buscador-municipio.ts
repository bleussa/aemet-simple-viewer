import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { Municipio } from '../../../../core/models/municipio';

@Component({
  selector: 'app-buscador-municipio',
  imports: [MatAutocompleteModule, MatFormFieldModule, MatInputModule],
  templateUrl: './buscador-municipio.html',
  styleUrl: './buscador-municipio.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BuscadorMunicipio {
  readonly municipios = input.required<Municipio[]>();
  readonly busquedaCambiada = output<string>();
  readonly municipioSeleccionado = output<Municipio>();

  protected readonly mostrarNombre = (municipio: Municipio | string | null): string =>
    typeof municipio === 'string' ? municipio : (municipio?.nombre ?? '');
}
