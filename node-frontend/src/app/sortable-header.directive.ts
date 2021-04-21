import {Directive, EventEmitter, Input, Output} from '@angular/core';

type SortDirection = 'asc' | 'desc' | '';

export interface SortEvent {
    column: string;
}

@Directive({
    selector: 'th[sortable]',
    host: {
        '[class.asc]': 'direction === "asc"',
        '[class.desc]': 'direction === "desc"',
        '(click)': 'rotate()'
    }
})
export class SortableHeaderDirective {

    @Input() sortable: string;
    @Input() direction: SortDirection = '';
    @Output() sort = new EventEmitter<SortEvent>();

    rotate() {
        this.direction = this.nextDirection(this.direction);
        this.sort.emit({column: this.sortable});
    }

    public process(column: string, data): void {
        if (this.sortable === column) {
            this.direction = this.nextDirection(this.direction);
            data.sort((a, b) => {
                const res = SortableHeaderDirective.compare(a[column], b[column]);
                return this.direction === 'asc' ? res : -res;
            });
        } else {
            this.direction = '';
        }
    }

    private static compare = (v1, v2) => v1 < v2 ? -1 : v1 > v2 ? 1 : 0;

    private nextDirection(current: SortDirection): SortDirection {
        if (current === 'asc') {
            return 'desc';
        }

        if (current === 'desc') {
            return '';
        }

        if (current === '') {
            return 'asc';
        }

        throw new Error(`Direction '${current}' is unknown.`);
    }

}
