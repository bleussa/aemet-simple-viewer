import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { Prediccion } from '../../../core/models/prediccion';
import { BuscadorMunicipio } from '../components/buscador-municipio/buscador-municipio';
import { SelectorUnidad } from '../components/selector-unidad/selector-unidad';
import { PrediccionPage } from './prediccion-page';

const ALCANIZ = { codigo: '44013', nombre: 'Alcañiz' };
const PREDICCION: Prediccion = {
  mediaTemperatura: 19,
  unidadTemperatura: 'G_CEL',
  probPrecipitacion: [
    { probabilidad: 5, periodo: '00-06' },
    { probabilidad: 70, periodo: '06-12' },
  ],
};

const esperar = (ms: number) => new Promise((resolver) => setTimeout(resolver, ms));

describe('PrediccionPage', () => {
  let fixture: ComponentFixture<PrediccionPage>;
  let http: HttpTestingController;

  const buscador = () =>
    fixture.debugElement.query(By.directive(BuscadorMunicipio))
      .componentInstance as BuscadorMunicipio;
  const selector = () =>
    fixture.debugElement.query(By.directive(SelectorUnidad)).componentInstance as SelectorUnidad;
  const texto = () => (fixture.nativeElement as HTMLElement).textContent ?? '';

  beforeEach(async () => {
    TestBed.configureTestingModule({
      imports: [PrediccionPage],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(PrediccionPage);
    await fixture.whenStable();
  });

  afterEach(() => http.verify());

  it('invita a buscar un municipio al empezar', () => {
    expect(texto()).toContain('Busca un municipio');
  });

  it('carga y muestra la predicción al elegir un municipio', async () => {
    buscador().municipioSeleccionado.emit(ALCANIZ);
    TestBed.tick();

    http.expectOne('/api/prediccion/44013').flush(PREDICCION);
    await fixture.whenStable();

    expect(texto()).toContain('Alcañiz');
    expect(texto()).toContain('19.0 °C');
    expect(texto()).toContain('06-12 h');
    expect(texto()).toMatch(/paraguas/);
  });

  it('recarga la predicción al cambiar la unidad', async () => {
    buscador().municipioSeleccionado.emit(ALCANIZ);
    TestBed.tick();
    http.expectOne('/api/prediccion/44013').flush(PREDICCION);
    await fixture.whenStable();

    selector().unidad.set('G_FAH');
    TestBed.tick();

    http
      .expectOne('/api/prediccion/44013?unidad=G_FAH')
      .flush({ ...PREDICCION, mediaTemperatura: 66.2, unidadTemperatura: 'G_FAH' });
    await fixture.whenStable();

    expect(texto()).toContain('66.2 °F');
  });

  it('recarga la predicción al cambiar de municipio', async () => {
    buscador().municipioSeleccionado.emit(ALCANIZ);
    TestBed.tick();
    http.expectOne('/api/prediccion/44013').flush(PREDICCION);

    buscador().municipioSeleccionado.emit({ codigo: '44216', nombre: 'Teruel' });
    TestBed.tick();

    http.expectOne('/api/prediccion/44216').flush(PREDICCION);
    await fixture.whenStable();
    expect(texto()).toContain('Teruel');
  });

  it('muestra un error si AEMET no responde', async () => {
    buscador().municipioSeleccionado.emit(ALCANIZ);
    TestBed.tick();

    http.expectOne('/api/prediccion/44013').flush(null, { status: 502, statusText: 'Bad Gateway' });
    await fixture.whenStable();

    expect(texto()).toContain('AEMET no responde');
  });

  it('busca municipios con debounce y a partir de 2 caracteres', async () => {
    buscador().busquedaCambiada.emit('a');
    TestBed.tick();
    await esperar(350);
    http.expectNone((req) => req.url === '/api/municipios');

    buscador().busquedaCambiada.emit('al');
    TestBed.tick();
    await esperar(100);
    buscador().busquedaCambiada.emit('alc');
    TestBed.tick();
    await esperar(350);

    http.expectOne((req) => req.url === '/api/municipios').flush([ALCANIZ]);
    await fixture.whenStable();
    expect(buscador().municipios()).toEqual([ALCANIZ]);
  });

  it('cancela la búsqueda anterior si llega una nueva', async () => {
    buscador().busquedaCambiada.emit('alc');
    TestBed.tick();
    await esperar(350);
    const primera = http.expectOne((req) => req.params.get('nombre') === 'alc');

    buscador().busquedaCambiada.emit('alca');
    TestBed.tick();
    await esperar(350);

    expect(primera.cancelled).toBe(true);
    http.expectOne((req) => req.params.get('nombre') === 'alca').flush([ALCANIZ]);
  });
});
