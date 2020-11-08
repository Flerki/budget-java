package com.amairovi.expense;

import com.amairovi.domain.Expense;
import com.amairovi.repository.ExpenseRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DefaultExpenseService implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    @NonNull
    public synchronized List<Expense> list() {
        log.debug("List all expenses");
        final List<Expense> expenses = expenseRepository.findAll();
        log.debug("{} expenses were retrieved: {}", expenses.size(), expenses);
        return expenses;
    }

    @Override
    @NonNull
    public void save(@NonNull final Expense expense) {
        expenseRepository.save(expense);
    }

}
