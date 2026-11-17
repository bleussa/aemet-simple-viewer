import { Prediccion } from '../models/prediccion';
import { aCelsius, LLUVIA_ALTA, LLUVIA_MEDIA, probabilidadMaxima, tramoTemperatura } from './clima';

export type TipoClima = 'sol' | 'calor' | 'frio' | 'nubes' | 'lluvia' | 'nieve';

export function tipoClima(prediccion: Prediccion): TipoClima {
  const tramo = tramoTemperatura(aCelsius(prediccion));
  const lluvia = probabilidadMaxima(prediccion);

  if (lluvia >= LLUVIA_ALTA) {
    return tramo === 'helada' ? 'nieve' : 'lluvia';
  }
  if (lluvia >= LLUVIA_MEDIA) {
    return 'nubes';
  }
  if (tramo === 'calor') {
    return 'calor';
  }
  return tramo === 'helada' || tramo === 'frio' ? 'frio' : 'sol';
}

export function tipoClimaFranja(probabilidad: number): TipoClima {
  if (probabilidad >= LLUVIA_ALTA) {
    return 'lluvia';
  }
  return probabilidad >= LLUVIA_MEDIA ? 'nubes' : 'sol';
}
