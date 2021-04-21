import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ExpenseService} from "../expense.service";
import * as _ from "lodash";


class Year {
    year: number;
    total: number;
    monthly: number;
}

@Component({
    selector: 'per-year',
    template: `

        <div style="width: 400px; margin-left: 20px; float: left" class="card">
            <div class="card-body">
                <h5 class="card-title">Expenses per year for {{category}}</h5>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Year</th>
                        <th scope="col">Total</th>
                        <th scope="col">Monthy Avg.</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr *ngFor="let entry of data" 
                        [class.chosen]="entry.year === chosenYear"
                        (click)="chooseYear(entry)">
                        <td>{{entry.year}}</td>
                        <td>{{entry.total | number:'0.2-2'}}</td>
                        <td>{{entry.monthly | number:'0.2-2'}}</td>
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
export class PerYearComponent implements OnChanges, OnInit {

    data: Year[] = [];
    chosenYear: number;

    @Input() category: string;
    @Output() yearChosenEvent = new EventEmitter<number>();

    constructor(private expenseService: ExpenseService) {
    }

    ngOnInit() {
        console.log(`ngOnInit started`);
        this.expenseService.expensesRefreshedSubject.subscribe(() => {
            console.log(`expenses refreshed event received`);
            this.refreshData();
        });
        this.refreshData();
        if (this.data.length > 0){
            this.chooseYear(this.data[0]);
        }

        console.log(`ngOnInit finished`);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.refreshData();
    }

    private refreshData() {
        this.data = _(this.expenseService.expenses)
            .filter(e => e.category === this.category)
            .groupBy(e => e.date.getFullYear())
            .map((expenses, year) => {
                const total = _(expenses).sumBy(e => e.sum);
                const monthly = total / 12;
                return {year: parseInt(year), total, monthly};
            })
            .value();
    }

    chooseYear(entry: any) {
        console.log('chooseYear ' + JSON.stringify(entry));
        this.chosenYear = entry.year;
        this.yearChosenEvent.emit(this.chosenYear);
    }

}
