import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class DateService {

    constructor() {
    }

    getMonthIndex(name: string): number {
        return this.getMonthNames().findIndex(el => el === name);
    }

    getMonthNames(): string[] {
        return [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11].map(this.getMonthName);
    }

    // month 0-based
    getMonthName(month: number): string {
        return new Date(0, month).toLocaleString('en', {month: 'long'});
    }

    // month 0-based
    getAmountOfDays(year: number, month: number): number {
        return new Date(year, month + 1, 0).getDate();
    }
}
