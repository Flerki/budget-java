package com.amairovi.web.expenses;


import com.amairovi.domain.Expense;
import com.amairovi.expense.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ListExpenseHandler implements Handler {

    private static final DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DecimalFormat sumFormat = new DecimalFormat("#0.##");

    private final ExpenseService expenseService;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(@NotNull final Context ctx) throws Exception {
        final List<ExpenseDto> dtos = expenseService.list()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        final String responseBody = objectMapper.writeValueAsString(dtos);
        ctx.status(HttpStatus.OK_200)
                .contentType("application/json")
                .result(responseBody);
    }

    @NonNull
    private ExpenseDto mapToDto(@NonNull Expense expense) {
        final Instant createdAt = expense.getCreatedAt();
        final LocalDate localDate = LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()).toLocalDate();
        final String createdAtStr = createdAtFormatter.format(localDate);

        final String sum = sumFormat.format(expense.getSum());
        return new ExpenseDto(createdAtStr, sum, expense.getCategory(), expense.getComment());
    }
}
