import { httpResource, HttpResourceRef } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Prediccion, UnidadTemperatura } from '../models/prediccion';

@Injectable({ providedIn: 'root' })
export class PrediccionApi {
  prediccion(
    codigoMunicipio: () => string | undefined,
    unidad: () => UnidadTemperatura | undefined,
  ): HttpResourceRef<Prediccion | undefined> {
    return httpResource<Prediccion>(() => {
      const codigo = codigoMunicipio();
      if (!codigo) {
        return undefined;
      }
      const unidadSeleccionada = unidad();
      return {
        url: `/api/prediccion/${codigo}`,
        params: unidadSeleccionada ? { unidad: unidadSeleccionada } : undefined,
      };
    });
  }
}
