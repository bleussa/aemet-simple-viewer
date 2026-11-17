import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, debounceTime, distinctUntilChanged, map, of, switchMap } from 'rxjs';

import { MunicipiosApi } from '../../../core/api/municipios-api';
import { Municipio } from '../../../core/models/municipio';
import { UnidadTemperatura } from '../../../core/models/prediccion';
import { BuscadorMunicipio } from '../components/buscador-municipio/buscador-municipio';
import { SelectorUnidad } from '../components/selector-unidad/selector-unidad';

const MINIMO_CARACTERES = 2;
const ESPERA_BUSQUEDA_MS = 300;

@Component({
  selector: 'app-prediccion-page',
  imports: [BuscadorMunicipio, SelectorUnidad],
  templateUrl: './prediccion-page.html',
  styleUrl: './prediccion-page.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PrediccionPage {
  private readonly municipiosApi = inject(MunicipiosApi);

  protected readonly busqueda = signal('');
  protected readonly municipio = signal<Municipio | undefined>(undefined);
  protected readonly unidad = signal<UnidadTemperatura | undefined>(undefined);

  protected readonly municipios = toSignal(
    toObservable(this.busqueda).pipe(
      debounceTime(ESPERA_BUSQUEDA_MS),
      map((texto) => texto.trim()),
      distinctUntilChanged(),
      switchMap((texto) =>
        texto.length < MINIMO_CARACTERES
          ? of([])
          : this.municipiosApi.buscar(texto).pipe(catchError(() => of([]))),
      ),
    ),
    { initialValue: [] },
  );
}
