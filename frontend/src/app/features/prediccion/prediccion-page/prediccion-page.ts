import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { catchError, debounceTime, distinctUntilChanged, map, of, switchMap } from 'rxjs';

import { MunicipiosApi } from '../../../core/api/municipios-api';
import { PrediccionApi } from '../../../core/api/prediccion-api';
import { Municipio } from '../../../core/models/municipio';
import { UnidadTemperatura } from '../../../core/models/prediccion';
import { BuscadorMunicipio } from '../components/buscador-municipio/buscador-municipio';
import { PrediccionResultado } from '../components/prediccion-resultado/prediccion-resultado';
import { SelectorUnidad } from '../components/selector-unidad/selector-unidad';

const MINIMO_CARACTERES = 2;
const ESPERA_BUSQUEDA_MS = 300;

@Component({
  selector: 'app-prediccion-page',
  imports: [BuscadorMunicipio, SelectorUnidad, PrediccionResultado, MatProgressBarModule],
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

  protected readonly prediccion = inject(PrediccionApi).prediccion(
    () => this.municipio()?.codigo,
    () => this.unidad(),
  );

  protected readonly resultado = computed(() =>
    this.prediccion.hasValue() ? this.prediccion.value() : undefined,
  );

  protected readonly mensajeError = computed(() => {
    const error = this.prediccion.error();
    if (!error) {
      return undefined;
    }
    const estado = error instanceof HttpErrorResponse ? error.status : 0;
    if (estado === 404) {
      return 'AEMET no tiene previsión para mañana en este municipio.';
    }
    if (estado === 502) {
      return 'AEMET no responde ahora mismo. Prueba de nuevo en un rato.';
    }
    return 'No hemos podido cargar la previsión.';
  });
}
