package com.amairovi.web.expenses;

import com.amairovi.domain.Expense;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ExpenseUtils {
    private static final DateTimeFormatter CREATED_AT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DecimalFormat SUM_FORMAT = new DecimalFormat("#0.##");

    static List<ExpenseDto> toDto(final List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseUtils::toDto)
                .collect(toList());
    }

    static ExpenseDto toDto(@NonNull Expense expense) {
        final Instant createdAt = expense.getCreatedAt();
        final LocalDate localDate = LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()).toLocalDate();
        final String createdAtStr = CREATED_AT_FORMATTER.format(localDate);

        final String sum = SUM_FORMAT.format(expense.getSum());
        return new ExpenseDto(createdAtStr, sum, expense.getCategory(), expense.getComment());
    }
}
