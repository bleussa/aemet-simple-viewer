import { ChangeDetectionStrategy, Component } from '@angular/core';

import { PrediccionPage } from './features/prediccion/prediccion-page/prediccion-page';

@Component({
  selector: 'app-root',
  imports: [PrediccionPage],
  template: '<app-prediccion-page />',
  styleUrl: './app.sass',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class App {}
