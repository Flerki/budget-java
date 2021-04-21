import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ExpenseService} from "../expense.service";
import * as _ from "lodash";
import {DateService} from "../date.service";


class Month {
    name: string;
    total: number;
    daily: number;
}

@Component({
    selector: 'per-month',
    template: `

        <div style="width: 400px; margin-left: 20px; float: left" class="card">
            <div class="card-body">
                <h5 class="card-title">За {{year}} год для {{category}}</h5>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Месяц</th>
                        <th scope="col">Всего</th>
                        <th scope="col">Ежедневно</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr *ngFor="let entry of data"
                        [class.chosen]="entry.name === chosenMonthName"
                        (click)="chooseMonth(entry.name)">
                        <td>{{entry.name}}</td>
                        <td>{{entry.total | number:'0.2-2'}}</td>
                        <td>{{entry.daily | number:'0.2-2'}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `,
    styles: [
        'tr {cursor: pointer;}',
        'tr:hover {background-color: #DDD;}',
        'tr.chosen {background-color: #EEE;}',
        'tr.chosen:hover {background-color: #DDD;}'
    ]
})
export class PerMonthComponent implements OnChanges, OnInit {

    data: Month[] = [];
    chosenMonthName: string;

    @Input() category: string;
    @Input() year: number = 2018;
    @Output() monthChosenEvent = new EventEmitter<number>();

    constructor(private expenseService: ExpenseService, private dateService: DateService) {
    }

    ngOnInit(): void {
        console.log(`ngOnInit started`);
        this.expenseService.expensesRefreshedSubject.subscribe(() => {
            console.log(`income refreshed event received`);
            this.refreshData();
        });
        this.refreshData();
        console.log(`ngOnInit finished`);
    }

    ngOnChanges(changes: SimpleChanges): void {
        console.log(`ngOnChanges started`);
        this.refreshData();
        const monthNames = this.dateService.getMonthNames();
        this.chooseMonth(monthNames[0]);

        console.log(`ngOnChanges finished`);
    }

    private refreshData() {
        let res = _(this.expenseService.expenses)
            .filter(e => e.category === this.category && e.date.getFullYear() === this.year)
            .groupBy(e => e.date.getMonth())
            .map((expenses, monthNumber) => {
                const total = _(expenses).sumBy(e => e.sum);
                const name = new Date(null, parseInt(monthNumber)).toLocaleString('en', {month: 'long'});
                const amountOfDays = this.dateService.getAmountOfDays(this.year, parseInt(monthNumber));
                const daily = total / amountOfDays;
                return {name, total, daily};
            })
            .keyBy(e => e.name)
            .value();

        this.data = [];
        const monthNames = this.dateService.getMonthNames();
        for (let i = 0; i < monthNames.length; i++) {
            const monthName = monthNames[i];
            if (res[monthName]) {
                this.data.push(res[monthName]);
            } else {
                this.data.push({name: monthName, total: 0, daily: 0});
            }
        }
    }

    chooseMonth(month: string) {
        this.chosenMonthName = month;
        const monthIndex = this.dateService.getMonthIndex(month);
        this.monthChosenEvent.emit(monthIndex);
    }
}
