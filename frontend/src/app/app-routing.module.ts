import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {SnitchComponent} from './snitch/snitch.component';
import {TestscenarioOverviewComponent} from './testscenario/testscenario-overview/testscenario-overview.component';

const routes: Routes = [
  { path: '',   redirectTo: '/testscenario', pathMatch: 'full' },
  { path: 'testscenario', component: TestscenarioOverviewComponent },
  { path: 'snitch', component: SnitchComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
