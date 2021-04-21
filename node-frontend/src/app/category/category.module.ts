import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PerYearComponent} from "./per-year.component";
import {PerMonthComponent} from "./per-month.component";
import {PerDayComponent} from "./per-day.component";
import {CategoryOverviewComponent} from "./category-overview.component";

@NgModule({
    declarations: [
        CategoryOverviewComponent,
        PerYearComponent,
        PerMonthComponent,
        PerDayComponent
    ],
    imports: [
        CommonModule
    ]
})
export class CategoryModule {
}
