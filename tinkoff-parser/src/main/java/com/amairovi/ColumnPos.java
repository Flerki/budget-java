package com.amairovi;

import java.util.Arrays;
import java.util.Optional;

public enum ColumnPos {
    ID(0),
    DATE(2),
    KIND(6),
    SHORTENED_NAME(7),
    TICKER(8),
    CURRENCY(10),
    AMOUNT(11),
    TOTAL_PRICE(12),
    BOND_ACCRUED_INTEREST(13),
    COMMISSION(16);

    public int pos;

    ColumnPos(int pos) {
        this.pos = pos;
    }

    public static Optional<ColumnPos> getByPos(int pos) {
        return Arrays.stream(values())
                .filter(it -> it.pos == pos)
                .findAny();
    }
}
