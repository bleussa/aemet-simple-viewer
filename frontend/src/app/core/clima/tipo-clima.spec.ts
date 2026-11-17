import { Prediccion, UnidadTemperatura } from '../models/prediccion';
import { tipoClima, tipoClimaFranja } from './tipo-clima';

function prediccion(
  mediaTemperatura: number,
  probabilidad = 0,
  unidadTemperatura: UnidadTemperatura = 'G_CEL',
): Prediccion {
  return {
    mediaTemperatura,
    unidadTemperatura,
    probPrecipitacion: [{ probabilidad, periodo: '12-18' }],
  };
}

describe('tipoClima', () => {
  it.each([
    [20, 0, 'sol'],
    [30, 0, 'calor'],
    [6, 0, 'frio'],
    [-1, 0, 'frio'],
    [20, 40, 'nubes'],
    [20, 80, 'lluvia'],
    [-1, 80, 'nieve'],
  ] as const)('con %s °C y %s %% de lluvia es %s', (temperatura, lluvia, esperado) => {
    expect(tipoClima(prediccion(temperatura, lluvia))).toBe(esperado);
  });

  it('evalúa la temperatura en Celsius aunque llegue en Fahrenheit', () => {
    expect(tipoClima(prediccion(86, 0, 'G_FAH'))).toBe('calor');
    expect(tipoClima(prediccion(30.2, 80, 'G_FAH'))).toBe('nieve');
  });

  it('considera soleado un día sin franjas de precipitación', () => {
    expect(
      tipoClima({ mediaTemperatura: 20, unidadTemperatura: 'G_CEL', probPrecipitacion: [] }),
    ).toBe('sol');
  });
});

describe('tipoClimaFranja', () => {
  it.each([
    [0, 'sol'],
    [29, 'sol'],
    [30, 'nubes'],
    [59, 'nubes'],
    [60, 'lluvia'],
  ] as const)('con %s %% es %s', (probabilidad, esperado) => {
    expect(tipoClimaFranja(probabilidad)).toBe(esperado);
  });
});
