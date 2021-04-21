import {
    Component,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output,
    QueryList,
    SimpleChanges,
    ViewChildren
} from '@angular/core';
import {SortableHeaderDirective, SortEvent} from "../sortable-header.directive";
import {IncomeService} from "../income.service";

@Component({
    selector: 'month-list',
    template: `
        <div>
            <div style="width: 400px;" class="card">
                <div class="card-body">
                    <h5 class="card-title">Per month</h5>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Month</th>
                            <th scope="col">Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr *ngFor="let entry of dataPerMonth"
                            [class.chosen]="entry.monthNumber === month"
                            (click)="chooseMonth(entry)">
                            <td>{{entry.month}}</td>
                            <td>{{entry.total | number:'0.2-2'}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div style="width: 400px; margin-top: 20px" class="card">
                <div class="card-body">
                    <h5 class="card-title">Per category</h5>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col" sortable="category" (sort)="onSort($event)">Category</th>
                            <th scope="col" sortable="total" (sort)="onSort($event)">Total</th>
                            <th scope="col" sortable="monthly" (sort)="onSort($event)">Month Avg.</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr *ngFor="let entry of dataPerCategory">
                            <td>{{entry.category}}</td>
                            <td>{{entry.total | number:'0.2-2'}}</td>
                            <td>{{entry.monthly | number:'0.2-2'}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
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
export class MonthListComponent implements OnChanges, OnInit {
    dataPerMonth = [];
    dataPerCategory = [];
    month: number = 1;

    @Input() year: number;
    @Output() monthChosenEvent = new EventEmitter<number>();
    @ViewChildren(SortableHeaderDirective) headers: QueryList<SortableHeaderDirective>;

    constructor(private incomeService: IncomeService) {
    }

    ngOnInit(): void {
        console.log(`ngOnInit started`);
        this.incomeService.incomeRefreshedSubject.subscribe(() => {
            console.log(`income refreshed event received`);
            this.refreshData();
        });
        this.refreshData();
        console.log(`ngOnInit finished`);
    }

    ngOnChanges(changes: SimpleChanges): void {
        console.log(`ngOnChanges started`);
        this.refreshData();
        console.log(`ngOnChanges finished`);
    }

    private refreshData() {
        console.log(`initData for year ${this.year} started`);
        this.dataPerMonth = this.incomeService.getPerMonth(this.year);
        this.dataPerCategory = this.incomeService.getPerCategory(this.year);
    }

    chooseMonth(entry: any) {
        const chosenMonth = entry.monthNumber;
        this.month = chosenMonth;
        this.monthChosenEvent.emit(chosenMonth);
    }

    onSort(event: SortEvent) {
        this.headers.forEach(header => {
            header.process(event.column, this.dataPerCategory);
        });
    }
}
