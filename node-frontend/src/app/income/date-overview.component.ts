import {Component} from '@angular/core';

@Component({
    selector: 'date-overview',
    template: `
        <year-list (yearChosenEvent)="chooseYear($event)"></year-list>
        <month-list [year]="currentYear" (monthChosenEvent)="chooseMonth($event)"></month-list>
        <month-overview [year]="currentYear" [month]="currentMonth"></month-overview>
    `,
    styles: [
        'year-list {float: left; margin-left: 20px}',
        'month-list {float: left; margin-left: 20px}',
        'month-overview {float: left; margin-left: 20px}',
    ]
})
export class DateOverviewComponent{
    currentMonth: number = 0;
    currentYear: number;

    chooseYear(year: number) {
        this.currentYear = year;
    }

    chooseMonth(month: number) {
        this.currentMonth = month;
    }
}
