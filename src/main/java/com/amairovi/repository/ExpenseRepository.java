package com.amairovi.repository;

import com.amairovi.domain.Expense;
import lombok.NonNull;

import java.util.List;

public interface ExpenseRepository {
    @NonNull
    List<Expense> findAll();
}
