package com.amairovi.expense;

import com.amairovi.domain.Expense;
import lombok.NonNull;

import java.util.List;

public interface ExpenseService {
    @NonNull
    List<Expense> list();

}
