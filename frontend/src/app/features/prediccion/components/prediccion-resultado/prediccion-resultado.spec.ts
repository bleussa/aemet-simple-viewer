import { TestBed } from '@angular/core/testing';

import { PrediccionResultado } from './prediccion-resultado';

describe('PrediccionResultado', () => {
  it('muestra la temperatura media, el consejo y las franjas con su icono', async () => {
    const fixture = TestBed.createComponent(PrediccionResultado);
    fixture.componentRef.setInput('municipio', { codigo: '28079', nombre: 'Madrid' });
    fixture.componentRef.setInput('prediccion', {
      mediaTemperatura: 66.7,
      unidadTemperatura: 'G_FAH',
      probPrecipitacion: [
        { probabilidad: 5, periodo: '00-06' },
        { probabilidad: 40, periodo: '06-12' },
        { probabilidad: 80, periodo: '12-18' },
        { probabilidad: 5, periodo: '18-24' },
      ],
    });
    await fixture.whenStable();

    const elemento = fixture.nativeElement as HTMLElement;
    expect(elemento.querySelector('.temperatura')?.textContent).toContain('66.7 °F');
    expect(elemento.querySelector('.consejo')?.textContent).toMatch(/paraguas/);
    expect(elemento.querySelectorAll('tbody tr').length).toBe(4);
    expect(
      [...elemento.querySelectorAll('tbody app-icono-clima svg')].map((svg) =>
        svg.getAttribute('aria-label'),
      ),
    ).toEqual(['Soleado', 'Nublado', 'Lluvia', 'Soleado']);
  });
});
