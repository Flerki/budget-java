import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {YearListComponent} from "./year-list.component";
import {MonthListComponent} from "./month-list.component";
import {MonthOverviewComponent} from "./month-overview.component";
import {DateOverviewComponent} from "./date-overview.component";


@NgModule({
    declarations: [
        YearListComponent,
        MonthListComponent,
        MonthOverviewComponent,
        DateOverviewComponent,
    ],
    imports: [
        CommonModule
    ]
})
export class DateModule {
}
