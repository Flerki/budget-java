import {Injectable} from '@angular/core';
import * as _ from "lodash";
import {DateService} from "./date.service";
import {map} from "rxjs/operators";
import {Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

interface Income {
    category: string;
    date: Date;
    sum: number;
}

@Injectable({
    providedIn: 'root'
})
export class IncomeService {
    static AMOUNT_OF_YEARS = 2;

    years: Set<number> = new Set();

    incomeRefreshedSubject: Subject<any> = new Subject<any>();

    income: Income[] = [];

    currentYear: number;

    constructor(private http: HttpClient,
                private dateService: DateService) {
        console.log(`constructor started`);
        this.refresh();
        console.log(`constructor finished`);
    }

    refresh() {
        this.http.get<any>(`${environment.backendHost}/income`)
            .pipe(
                map(data => {
                    console.log(`data was loaded`);

                    data.forEach(e => {
                        e.category = e.category.trim();
                        const [date, month, year] = e.date.split(".");
                        e.date = new Date(year, month - 1, date);

                        this.years.add(year);
                    });

                    IncomeService.AMOUNT_OF_YEARS = this.years.size;
                    this.currentYear = Math.max(...Array.from(this.years));

                    return data;
                })
            )
            .subscribe(data => {
                this.years.clear();
                this.income = data;
                this.incomeRefreshedSubject.next();
            });
    }

    getPerYear() {
        return _(this.income)
            .groupBy(e => e.date.getFullYear())
            .map((expenses, year) => {
                const total = _(expenses).sumBy(e => e.sum);
                const monthlyAverage = total / 12;
                return {year: parseInt(year), total, monthlyAverage};
            })
            .value();
    }

    getPerMonth(year: number) {
        return _(this.income)
            .filter(e => e.date.getFullYear() === year)
            .groupBy(e => e.date.getMonth())
            .map((expenses, monthNumber) => {
                const total = _(expenses).sumBy(e => e.sum);
                const month = new Date(null, parseInt(monthNumber)).toLocaleString('en', {month: 'long'});
                return {month, total, monthNumber: parseInt(monthNumber)};
            })
            .value();
    }

    getPerCategory(year: number) {
        const AMOUNT_OF_MONTHS = 12;
        return _(this.income)
            .filter(e => e.date.getFullYear() === year)
            .groupBy(e => e.category)
            .map((expenses, category) => {
                const total = _(expenses).sumBy(e => e.sum);
                const monthly = total / AMOUNT_OF_MONTHS;
                return {category, total, monthly};
            })
            .sortBy(['category'])
            .value();
    }

    getOverviewForYearAndMonth(year: number, month: number) {
        const amountOfDays = this.dateService.getAmountOfDays(year, month);
        return _(this.income)
            .filter(e =>
                e.date.getFullYear() === year && e.date.getMonth() === month
            )
            .groupBy(e => e.category)
            .map((expenses, category) => {
                const total = _(expenses).sumBy(e => e.sum);
                const daily = total / amountOfDays;
                return {category, total, daily};
            })
            .sortBy(['category'])
            .value();
    }
}
