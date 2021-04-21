import {Component, DoCheck} from '@angular/core';
import * as _ from "lodash";

@Component({
    selector: 'mortgage-overview',
    template: `
        <div style="width: 4000px">
            <div>
                <div style="float: left;">

                    <div *ngFor="let e of data" class="card" style="width: 30rem;margin-top: 10px">
                        <div class="card-body">
                            <h5 class="card-title">{{e.date | date :'MMM yyyy'}}</h5>
                            <p class="card-text">Общая сумма платежа: {{e.total | number:'0.2-2'}}</p>
                            <p class="card-text">Процентная часть платежа: {{e.interest | number:'0.2-2'}}</p>
                            <p class="card-text">Основная часть платежа: {{e.real | number:'0.2-2'}}</p>
                            <p *ngIf="e.extraPayments.length > 0">Дополнительные платежи:</p>
                            <p *ngIf="e.extraPayments.length === 0">Дополнительных платежей нет.</p>
                            <ul *ngFor="let payment of e.additionalPayments">
                                <li class="card-text"> {{payment.number | number:'0.2-2'}} - {{payment.description}}
                                    - {{payment.type === 'one-time' ? 'однократный' : 'многократный'}}</li>
                            </ul>
                            <p class="card-text">Остаток: {{e.left | number:'0.2-2'}}</p>
                            <a class="btn btn-primary" (click)="openModal(e)" data-toggle="modal"
                               data-target="#exampleModal">Добавить платеж</a>
                        </div>
                    </div>
                </div>
            </div>


            <div class="modal fade" id="exampleModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Добавить платеж</h5>
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <p>Дата: {{modalDate | date :'MMM yyyy'}}</p>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">@</span>
                                </div>
                                <input type="text" class="form-control" placeholder="Размер платежа"
                                       [(ngModel)]="modalPayment">
                            </div>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">@</span>
                                </div>
                                <input type="text" class="form-control" placeholder="Описание платежа"
                                       [(ngModel)]="modalPaymentDescription">
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="payment-type" id="payment-type-1"
                                       value="one-time" [(ngModel)]="modalPaymentType">
                                <label class="form-check-label" for="payment-type-1">
                                    Однократный
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="payment-type" id="payment-type-2"
                                       value="multiple-time" [(ngModel)]="modalPaymentType">
                                <label class="form-check-label" for="payment-type-2">
                                    Многократный
                                </label>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal" (click)="addPayment()">
                                Сохранить
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div>
                <div *ngFor="let model of models" style="float: left; margin-left: 20px">

                    <div class="card" style="width: 30rem;margin-top: 10px">
                        <div class="card-body">
                            <h5 class="card-title">{{model.title}}</h5>
                            <p>Loan: {{model.total}}</p>
                            <p>Процент (годовые): {{model.interest}}</p>
                            <p>Размер платежа: {{model.payment}}</p>
                            <p>Monthly interest : {{model.monthlyInterest | number:'0.4-6'}}</p>
                            <p>Overpayment : {{model.overpayment | number:'0.2-2'}}</p>
                            <p>Amount of months : {{model.amountOfMonths}}</p>
                            <p>Дополнительные платежи :</p>
                            <ul>
                                <li *ngFor="let payment of model.extraPayments">{{payment.description}}</li>
                            </ul>
                        </div>
                    </div>

                    <div *ngFor="let e of model.payments" class="card" style="width: 30rem;margin-top: 10px">
                        <div class="card-body">
                            <h5 class="card-title">{{e.date | date :'MMM yyyy'}}</h5>
                            <p class="card-text">Общая сумма платежа: {{e.total | number:'0.2-2'}}</p>
                            <p class="card-text">Процентная часть платежа: {{e.interest | number:'0.2-2'}}</p>
                            <p class="card-text">Основная часть платежа: {{e.real | number:'0.2-2'}}</p>
                            <p *ngIf="extraPayments.length > 0">Дополнительные платежи:</p>
                            <p *ngIf="extraPayments.length === 0">Дополнительных платежей нет.</p>
                            <ul *ngFor="let payment of e.additionalPayments">
                                <li class="card-text"> {{payment.number | number:'0.2-2'}} - {{payment.description}}
                                    - {{payment.type === 'one-time' ? 'однократный' : 'многократный'}}</li>
                            </ul>
                            <p class="card-text">Остаток: {{e.left | number:'0.2-2'}}</p>
                            <a class="btn btn-primary" (click)="openModal(e)" data-toggle="modal"
                               data-target="#exampleModal">Добавить платеж</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    `,
    styles: []
})
export class MortgageComponent implements DoCheck {
    models = [

    ];

    extraPayments = [

    ];

    modalDate: Date;
    modalPayment: string = undefined;
    modalPaymentDescription: string;
    modalPaymentType: string = 'one-time';

    ngDoCheck(): void {
        console.log("ngDoCheck");
        this.models.forEach(e => this.calcModel(e));
    }

    calcModel(model: any) {
        model.monthlyInterest = model.interest / 100 / 12;

        let overpayment = 0;
        let amountOfMonths = 0;
        let payments = [];

        let total = model.total;
        let date = new Date();
        while (total > 0 && amountOfMonths < 100) {
            amountOfMonths++;
            const interestPart = total * model.monthlyInterest;
            overpayment += interestPart;
            const realPart = model.payment - interestPart;

            const additionalPayments = this.getAdditionalPaymentsFromModel(date, model);
            const additionPaymentTotal = _.sum(additionalPayments.map(p => p.number));

            total -= realPart + additionPaymentTotal;
            payments.push({
                date: date,
                total: model.payment + additionPaymentTotal,
                interest: interestPart,
                real: realPart,
                left: total,
                additionalPayments
            });

            if (date.getMonth() == 11) {
                date = new Date(date.getFullYear() + 1, 0, 1);
            } else {
                date = new Date(date.getFullYear(), date.getMonth() + 1, 1);
            }
        }

        model.overpayment = overpayment;
        model.amountOfMonths = amountOfMonths;
        model.payments = payments;

    }

    openModal(entry: any) {
        this.modalDate = entry.date;
    }

    addPayment() {
        const date = this.modalDate;

        const payment = {
            from: date,
            type: this.modalPaymentType,
            number: Number.parseFloat(this.modalPayment),
            description: this.modalPaymentDescription
        };

        this.extraPayments.push(payment);
    }

    getAdditionalPaymentsFromModel(date: Date, model: any): any[] {
        const oneTimePayments = model.extraPayments
            .filter(p => p.type === 'one-time')
            .filter(p => p.from.getFullYear() === date.getFullYear() && p.from.getMonth() === date.getMonth());

        const multipleTimePayments = model.extraPayments
            .filter(p => p.type === 'multiple-time')
            .filter(p => p.from.getFullYear() < date.getFullYear() ||
                (p.from.getFullYear() === date.getFullYear() && p.from.getMonth() <= date.getMonth()));
        return [...oneTimePayments, ...multipleTimePayments];
    }
}
