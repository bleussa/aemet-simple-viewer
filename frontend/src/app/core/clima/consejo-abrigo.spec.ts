import { Prediccion, UnidadTemperatura } from '../models/prediccion';
import { consejoAbrigo } from './consejo-abrigo';

function prediccion(
  mediaTemperatura: number,
  unidadTemperatura: UnidadTemperatura = 'G_CEL',
  probabilidades: number[] = [0],
): Prediccion {
  return {
    mediaTemperatura,
    unidadTemperatura,
    probPrecipitacion: probabilidades.map((probabilidad, i) => ({
      probabilidad,
      periodo: ['00-06', '06-12', '12-18', '18-24'][i],
    })),
  };
}

describe('consejoAbrigo', () => {
  it.each([
    [-2, /plumas|cebolla|pack completo/],
    [6, /chaquetón|lamentos|Jersey gordo/],
    [13, /rasca|entretiempo|estorbe/],
    [20, /anuncio|excusas|descanso/],
    [30, /ligera|crema solar|aire acondicionado/],
  ])('elige el tramo adecuado para %s °C', (temperatura, esperado) => {
    expect(consejoAbrigo(prediccion(temperatura), '44216')).toMatch(esperado);
  });

  it('convierte a Celsius antes de comparar si llega en Fahrenheit', () => {
    expect(consejoAbrigo(prediccion(86, 'G_FAH'), '44216')).toBe(
      consejoAbrigo(prediccion(30), '44216'),
    );
    expect(consejoAbrigo(prediccion(28.4, 'G_FAH'), '44216')).toBe(
      consejoAbrigo(prediccion(-2), '44216'),
    );
  });

  it('añade el paraguas si la probabilidad de lluvia es alta', () => {
    expect(consejoAbrigo(prediccion(13, 'G_CEL', [10, 80, 20, 0]), '44216')).toMatch(/paraguas/);
  });

  it('no menciona el paraguas si la probabilidad de lluvia es baja', () => {
    expect(consejoAbrigo(prediccion(13, 'G_CEL', [10, 20, 20, 0]), '44216')).not.toMatch(
      /paraguas/,
    );
  });

  it('es determinista para el mismo municipio', () => {
    expect(consejoAbrigo(prediccion(13), '28079')).toBe(consejoAbrigo(prediccion(13), '28079'));
  });

  it('puede variar la frase entre municipios', () => {
    const frases = new Set(
      ['00001', '00002', '00003'].map((c) => consejoAbrigo(prediccion(13), c)),
    );
    expect(frases.size).toBeGreaterThan(1);
  });
});
