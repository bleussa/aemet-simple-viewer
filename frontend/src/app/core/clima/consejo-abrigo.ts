import { Prediccion } from '../models/prediccion';
import {
  aCelsius,
  LLUVIA_ALTA,
  probabilidadMaxima,
  TramoTemperatura,
  tramoTemperatura,
} from './clima';

const FRASES: Record<TramoTemperatura, string[]> = {
  helada: [
    'Mañana hace un frío de replantearse la vida. Abrigo, bufanda, guantes y gorro: el pack completo.',
    'Frío polar a la vista. Sé como la cebolla: capas, capas y más capas.',
    'Si tienes un plumas de esos que parecen un saco de dormir, mañana es su gran día.',
  ],
  frio: [
    'Hace frío del serio. Abrigo y bufanda, que luego vienen los lamentos.',
    'Día de chaquetón. No te hagas el valiente, que nadie reparte medallas por pasar frío.',
    'Frío del que se mete en los huesos. Jersey gordo y abrigo encima, sin negociar.',
  ],
  fresco: [
    'Ni frío ni calor, pero tirando a rasca. Con una chaqueta vas sobrado.',
    'Tiempo de entretiempo, ese gran clásico. Chaqueta ligera y a la calle.',
    'Por la mañana refresca y a mediodía te sobra todo. Ve por capas y quítate lo que estorbe.',
  ],
  agradable: [
    'Temperatura de anuncio. Algo finito y listo, el abrigo se queda en casa.',
    'Ni frío ni calor: clima perfecto para no tener excusas. Ropa cómoda y a disfrutar.',
    'Mañana pinta bien. Deja el abrigo en el armario, que se ha ganado un descanso.',
  ],
  calor: [
    'Calor del bueno. Ropa ligera, agua a mano y la sombra como mejor amiga.',
    'Mañana aprieta. Camiseta, pantalón corto y crema solar, que luego todo son llantos.',
    'Calor de ir buscando tiendas con aire acondicionado. Lo mínimo imprescindible y fresquito.',
  ],
};

const FRASES_LLUVIA = [
  'Y coge paraguas, que mañana el cielo viene con ganas de llorar.',
  'Ah, y chubasquero o paraguas: va a caer agua y no apetece llegar como un pollo mojado.',
];

function elegir(frases: string[], semilla: string): string {
  const indice = [...semilla].reduce((suma, caracter) => suma + caracter.charCodeAt(0), 0);
  return frases[indice % frases.length];
}

export function consejoAbrigo(prediccion: Prediccion, codigoMunicipio: string): string {
  const consejo = elegir(FRASES[tramoTemperatura(aCelsius(prediccion))], codigoMunicipio);
  return probabilidadMaxima(prediccion) >= LLUVIA_ALTA
    ? `${consejo} ${elegir(FRASES_LLUVIA, codigoMunicipio)}`
    : consejo;
}
