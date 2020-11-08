package com.amairovi.web.expenses;


import com.amairovi.domain.Expense;
import com.amairovi.exception.ServiceException;
import com.amairovi.expense.ExpenseService;
import com.amairovi.web.ErrorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Slf4j
public class CreateExpenseHandler implements Handler {

    private static final DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DecimalFormat sumFormat = new DecimalFormat("#0.##");

    private final ExpenseService expenseService;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(@NotNull final Context ctx) throws Exception {
        final String body = ctx.body();
        try {
            final Expense expense = mapToEntity(body);
            expenseService.save(expense);

            // later on id field could be added
            // it'll be initialized within save
            final String result = objectMapper.writeValueAsString(mapToDto(expense));
            ctx.status(HttpStatus.CREATED_201)
                    .contentType("application/json")
                    .result(result);
        } catch (ServiceException e){
            final ErrorDto errorDto = new ErrorDto(e.getError().getCode(), e.getMessage());
            final String result = objectMapper.writeValueAsString(errorDto);

            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR_500)
                    .contentType("application/json")
                    .result(result);
        }
    }

    private Expense mapToEntity(final String body) throws JsonProcessingException {
        final JsonNode obj = objectMapper.readTree(body);
        final String date = obj.get("date").asText();
        final Instant createdAt = LocalDate.parse(date, createdAtFormatter)
                .atStartOfDay().toInstant(ZoneOffset.UTC);
        final BigDecimal sum = new BigDecimal(obj.get("sum").asText());
        final String category = obj.get("category").asText();
        final String comment = obj.get("comment").asText();

        return Expense.builder()
                .createdAt(createdAt)
                .sum(sum)
                .category(category)
                .comment(comment)
                .build();
    }

    @NonNull
    //TODO create a separate mapper
    private ExpenseDto mapToDto(@NonNull Expense expense) {
        final Instant createdAt = expense.getCreatedAt();
        final LocalDate localDate = LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()).toLocalDate();
        final String createdAtStr = createdAtFormatter.format(localDate);

        final String sum = sumFormat.format(expense.getSum());
        return new ExpenseDto(createdAtStr, sum, expense.getCategory(), expense.getComment());
    }
}
