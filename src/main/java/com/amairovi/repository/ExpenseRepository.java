package com.amairovi.repository;

import com.amairovi.domain.Expense;
import lombok.NonNull;

import java.util.List;

public interface ExpenseRepository {
    @NonNull
    List<Expense> findAll();

    @NonNull
    Expense save(@NonNull Expense expense);

    void removeAll();
}
