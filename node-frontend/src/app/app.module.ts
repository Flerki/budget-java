import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import { HttpClientModule }    from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SortableHeaderDirective} from './sortable-header.directive';
import {MortgageComponent} from "./mortgage/mortgage.component";
import {CategoryModule} from "./category/category.module";
import {DateModule} from "./date/date.module";
import {IncomeModule} from "./income/income.module";

@NgModule({
    declarations: [
        AppComponent,
        SortableHeaderDirective,
        MortgageComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,

        CategoryModule,
        DateModule,
        IncomeModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
