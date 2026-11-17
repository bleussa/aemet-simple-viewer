import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { Municipio } from '../models/municipio';
import { MunicipiosApi } from './municipios-api';

describe('MunicipiosApi', () => {
  let api: MunicipiosApi;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    api = TestBed.inject(MunicipiosApi);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('busca municipios por nombre', () => {
    let resultado: Municipio[] | undefined;
    api.buscar('alcañ').subscribe((municipios) => (resultado = municipios));

    const peticion = http.expectOne(
      (req) => req.url === '/api/municipios' && req.params.get('nombre') === 'alcañ',
    );
    expect(peticion.request.method).toBe('GET');
    peticion.flush([{ codigo: '44013', nombre: 'Alcañiz' }]);

    expect(resultado).toEqual([{ codigo: '44013', nombre: 'Alcañiz' }]);
  });
});
