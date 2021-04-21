import {Component, Input, OnChanges, OnInit, QueryList, SimpleChanges, ViewChildren} from '@angular/core';
import * as _ from "lodash";
import {DateService} from "../date.service";
import {SortableHeaderDirective, SortEvent} from "../sortable-header.directive";
import {IncomeService} from "../income.service";

@Component({
    selector: 'month-overview',
    template: `
        <div style="width: 400px;" class="card">
            <div class="card-body">
                <h5 class="card-title">Overview for {{monthName}} of {{year}}</h5>
                <div>
                    <p>Total: {{total | number:'0.2-2'}}</p>
                    <p>Amount of days: {{amountOfDays}}</p>
                    <p>Daily average: {{daily | number:'0.2-2'}}</p>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col" sortable="category" (sort)="onSort($event)">Category</th>
                        <th scope="col" sortable="total" (sort)="onSort($event)">Total</th>
                        <th scope="col" sortable="daily" (sort)="onSort($event)">Daily</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr *ngFor="let entry of data">
                        <td>{{entry.category}}</td>
                        <td>{{entry.total | number:'0.2-2'}}</td>
                        <td>{{entry.daily | number:'0.2-2'}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `,
    styles: []
})
export class MonthOverviewComponent implements OnInit, OnChanges {
    @ViewChildren(SortableHeaderDirective) headers: QueryList<SortableHeaderDirective>;

    @Input() year: number;

    @Input() month: number;

    data = [];
    total = 0;
    daily = 0;
    monthName: string;
    amountOfDays: number;

    constructor(private incomeService: IncomeService,
                private dateService: DateService) {
    }

    ngOnInit() {
        console.log(`ngOnInit started`);
        this.incomeService.incomeRefreshedSubject.subscribe(() => {
            console.log(`expenses refreshed event received`);
            this.refreshData();
        });
        this.refreshData();
        console.log(`ngOnInit finished`);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.refreshData();
    }

    private refreshData() {
        this.monthName = this.dateService.getMonthName(this.month);
        this.amountOfDays = this.dateService.getAmountOfDays(this.year, this.month);
        this.data = this.incomeService.getOverviewForYearAndMonth(this.year, this.month);
        this.total = _(this.data).sumBy(e => e.total);
        this.daily = this.total / this.amountOfDays;
    }

    onSort(event: SortEvent) {
        this.headers.forEach(header => {
            header.process(event.column, this.data);
        });
    }
}
