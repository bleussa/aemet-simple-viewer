import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ApplicationRef, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';

import { Prediccion, UnidadTemperatura } from '../models/prediccion';
import { PrediccionApi } from './prediccion-api';

const PREDICCION: Prediccion = {
  mediaTemperatura: 19,
  unidadTemperatura: 'G_CEL',
  probPrecipitacion: [{ probabilidad: 5, periodo: '00-06' }],
};

describe('PrediccionApi', () => {
  let http: HttpTestingController;
  const codigo = signal<string | undefined>(undefined);
  const unidad = signal<UnidadTemperatura | undefined>(undefined);

  function crearRecurso() {
    return TestBed.runInInjectionContext(() =>
      TestBed.inject(PrediccionApi).prediccion(codigo, unidad),
    );
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    codigo.set(undefined);
    unidad.set(undefined);
  });

  afterEach(() => http.verify());

  it('no hace ninguna petición sin municipio', () => {
    const recurso = crearRecurso();
    TestBed.tick();

    http.expectNone(() => true);
    expect(recurso.value()).toBeUndefined();
  });

  it('pide la predicción sin unidad si no hay ninguna seleccionada', async () => {
    const recurso = crearRecurso();
    codigo.set('44001');
    TestBed.tick();

    const peticion = http.expectOne('/api/prediccion/44001');
    expect(peticion.request.params.has('unidad')).toBe(false);
    peticion.flush(PREDICCION);
    await TestBed.inject(ApplicationRef).whenStable();

    expect(recurso.value()).toEqual(PREDICCION);
  });

  it('envía la unidad y vuelve a pedir la predicción cuando cambia', () => {
    crearRecurso();
    codigo.set('44001');
    unidad.set('G_CEL');
    TestBed.tick();
    http.expectOne('/api/prediccion/44001?unidad=G_CEL').flush(PREDICCION);

    unidad.set('G_FAH');
    TestBed.tick();

    http.expectOne('/api/prediccion/44001?unidad=G_FAH');
  });
});
