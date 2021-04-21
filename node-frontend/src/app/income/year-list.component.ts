import {Component, EventEmitter, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
import {SortableHeaderDirective} from "../sortable-header.directive";
import {IncomeService} from "../income.service";

@Component({
    selector: 'year-list',
    template: `
        <div>
            <div style="width: 400px" class="card">
                <div class="card-body">
                    <h5 class="card-title">Expenses per year</h5>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Year</th>
                            <th scope="col">Total</th>
                            <th scope="col">Month Avg.</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr *ngFor="let entry of data" [class.chosen]="entry.year === year" (click)="chooseYear(entry)">
                            <td>{{entry.year}}</td>
                            <td>{{entry.total | number:'0.2-2'}}</td>
                            <td>{{entry.monthlyAverage | number:'0.2-2'}}</td>
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
export class YearListComponent implements OnInit {
    data = [];
    year: number;
    @Output() yearChosenEvent = new EventEmitter<number>();
    @ViewChildren(SortableHeaderDirective) headers: QueryList<SortableHeaderDirective>;

    constructor(private incomeService: IncomeService) {
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

    private refreshData() {
        this.data = this.incomeService.getPerYear();
        const currentYear = this.incomeService.currentYear;
        this.chooseYear({year: currentYear});
    }

    chooseYear(yearInfo) {
        const chosenYear = yearInfo.year;
        this.year = chosenYear;
        this.yearChosenEvent.emit(chosenYear);
    }
}
