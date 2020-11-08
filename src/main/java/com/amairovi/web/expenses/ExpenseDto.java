package com.amairovi.web.expenses;

import lombok.Value;

@Value
public class ExpenseDto {
    String date;
    String sum;
    String category;
    String comment;
}
