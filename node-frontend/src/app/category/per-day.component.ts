import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ExpenseService} from "../expense.service";
import * as _ from "lodash";

@Component({
    selector: 'per-day',
    template: `
        <div style="width: 400px; margin-left: 20px; float: left">
            <div class="card" style="margin-bottom: 10px" *ngFor="let entry of data">
                <div class="card-body">
                    <h5 class="card-title">{{entry.date | date :'dd MMM yyyy'}}</h5>
                    <p>Сумма: {{entry.sum | number:'0.2-2'}}</p>
                    <p>Комментарий: {{entry.comment}}</p>
                </div>
            </div>
        </div>
    `,
    styles: []
})
export class PerDayComponent implements OnChanges, OnInit {

    data: any[] = [];

    @Input() category: string;
    @Input() year: number;
    @Input() month: number = 0;

    constructor(private expenseService: ExpenseService) {
    }

    ngOnInit(): void {
        console.log(`ngOnInit started`);
        this.expenseService.expensesRefreshedSubject.subscribe(() => this.refreshData());
        this.refreshData();
        console.log(`ngOnInit finished`);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.refreshData();
    }

    private refreshData() {
        this.data = _(this.expenseService.expenses)
            .filter(e => e.category === this.category && e.date.getFullYear() === this.year && e.date.getMonth() === this.month)
            .value();
    }
}
