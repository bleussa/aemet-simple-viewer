import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Municipio } from '../models/municipio';

@Injectable({ providedIn: 'root' })
export class MunicipiosApi {
  private readonly http = inject(HttpClient);

  buscar(nombre: string): Observable<Municipio[]> {
    return this.http.get<Municipio[]>('/api/municipios', { params: { nombre } });
  }
}
