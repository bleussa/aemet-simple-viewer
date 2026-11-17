import { Prediccion } from '../models/prediccion';

export type TramoTemperatura = 'helada' | 'frio' | 'fresco' | 'agradable' | 'calor';

export const LLUVIA_MEDIA = 30;
export const LLUVIA_ALTA = 60;

const LIMITES_TRAMOS: [number, TramoTemperatura][] = [
  [3, 'helada'],
  [10, 'frio'],
  [16, 'fresco'],
  [25, 'agradable'],
];

export function aCelsius(prediccion: Prediccion): number {
  return prediccion.unidadTemperatura === 'G_FAH'
    ? ((prediccion.mediaTemperatura - 32) * 5) / 9
    : prediccion.mediaTemperatura;
}

export function tramoTemperatura(celsius: number): TramoTemperatura {
  return LIMITES_TRAMOS.find(([limite]) => celsius < limite)?.[1] ?? 'calor';
}

export function probabilidadMaxima(prediccion: Prediccion): number {
  return Math.max(0, ...prediccion.probPrecipitacion.map((franja) => franja.probabilidad));
}
