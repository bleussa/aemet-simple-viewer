import { TestBed } from '@angular/core/testing';

import { IconoClima } from './icono-clima';

describe('IconoClima', () => {
  it('dibuja el tipo de clima con su descripción y tamaño', async () => {
    const fixture = TestBed.createComponent(IconoClima);
    fixture.componentRef.setInput('tipo', 'lluvia');
    fixture.componentRef.setInput('tamano', 28);
    await fixture.whenStable();

    const svg = (fixture.nativeElement as HTMLElement).querySelector('svg')!;
    expect(svg.getAttribute('aria-label')).toBe('Lluvia');
    expect(svg.getAttribute('width')).toBe('28');
    expect(svg.querySelectorAll('.gotas line').length).toBe(3);
  });
});
