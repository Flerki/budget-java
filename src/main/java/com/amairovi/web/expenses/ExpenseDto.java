package com.amairovi.web.expenses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private String date;
    private String sum;
    private String category;
    private String comment;
}
