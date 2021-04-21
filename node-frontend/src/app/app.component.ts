import {Component} from '@angular/core';
import {ExpenseService} from "./expense.service";
import {IncomeService} from "./income.service";

@Component({
    selector: 'app-root',
    template: `
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" routerLink="/">Budget</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item" routerLinkActive="active">
                        <a class="nav-link" routerLink="/date">Date</a>
                    </li>
                    <li class="nav-item" routerLinkActive="active">
                        <a class="nav-link" routerLink="/category">Category</a>
                    </li>
                    <li class="nav-item" routerLinkActive="income">
                        <a class="nav-link" routerLink="/income">Income</a>
                    </li>
                    <li class="nav-item" routerLinkActive="active">
                        <a class="nav-link" routerLink="/mortgage">Mortgage</a>
                    </li>
                </ul>
            </div>
            <div>
                <button (click)="refreshExpenses()">Refresh expenses</button>
                <button (click)="refreshIncome()">Refresh income</button>
            </div>
        </nav>
        <div style="margin-top: 10px">
            <router-outlet></router-outlet>
        </div>
    `
})
export class AppComponent {

    constructor(private expenseService: ExpenseService, private incomeService: IncomeService) {
    }

    refreshExpenses() {
        this.expenseService.refresh();
    }
    refreshIncome() {
        this.incomeService.refresh();
    }
}
