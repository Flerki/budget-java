package com.amairovi.config;

import com.amairovi.expense.DefaultExpenseService;
import com.amairovi.expense.ExpenseService;
import com.amairovi.repository.DefaultExpenseRepository;
import com.amairovi.repository.ExpenseRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExpenseConfiguration {

    public ExpenseRepository expenseRepository(ExpenseProperties expenseProperties) {
        final Path pathToExpenseFile = Paths.get(expenseProperties.getPathToExpenseFile());
        return new DefaultExpenseRepository(pathToExpenseFile);
    }

    public ExpenseService expenseService(ExpenseRepository expenseRepository) {
        return new DefaultExpenseService(expenseRepository);
    }
}
