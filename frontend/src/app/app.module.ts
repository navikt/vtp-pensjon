import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AccessLogComponent } from './snitch/access-log.component';
import { SnitchComponent } from './snitch/snitch.component';
import {HttpClientModule} from "@angular/common/http";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AtobPipe } from './atob.pipe';
import { JsonparsePipe } from './jsonparse.pipe';
import { TestscenarioOverviewComponent } from './testscenario/testscenario-overview/testscenario-overview.component';
import {FormsModule} from '@angular/forms';
import { PayloadComponent } from './snitch/payload.component';
import { SummaryComponent } from './snitch/summary.component';
import { HeadersComponent } from './snitch/headers.component';
import { XmlformatterPipe } from './xmlformatter.pipe';

@NgModule({
  declarations: [
    AppComponent,
    AccessLogComponent,
    SnitchComponent,
    AtobPipe,
    JsonparsePipe,
    TestscenarioOverviewComponent,
    PayloadComponent,
    HeadersComponent,
    SummaryComponent,
    XmlformatterPipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
