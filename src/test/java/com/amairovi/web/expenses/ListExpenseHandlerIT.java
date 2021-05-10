package com.amairovi.web.expenses;

import com.amairovi.domain.Expense;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amairovi.web.expenses.ExpenseUtils.toDto;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ListExpenseHandlerIT extends ITProto {

    private final static TypeReference<List<ExpenseDto>> EXPENSE_DTO_LIST_TYPE = new TypeReference<List<ExpenseDto>>() {
    };

    private final Random random = new Random();

    @Test
    void shouldReturnEmptyArrayWhenThereIsNoExpense() throws IOException {
        Request request = new Request.Builder()
                .url(HOST + "/expenses")
                .get()
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(HttpStatus.OK_200);
            String responseBody = Objects.requireNonNull(response.body()).string();
            assertThat(responseBody).isEqualTo("[]");
        }
    }

    @Test
    void shouldReturnAllExpensesWhenThereAreExpenses() throws IOException {
        int amountOfExpenses = 10;
        List<Expense> expected = IntStream.range(0, amountOfExpenses)
                .mapToObj(ignored -> expenseRepository.save(createExpense()))
                .collect(Collectors.toList());

        Request request = new Request.Builder()
                .url(HOST + "/expenses")
                .get()
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(HttpStatus.OK_200);
            String responseBody = Objects.requireNonNull(response.body()).string();
            List<ExpenseDto> actual = OBJECT_MAPPER.readValue(responseBody, EXPENSE_DTO_LIST_TYPE);
            assertThat(actual).hasSize(amountOfExpenses);

            List<ExpenseDto> expectedDtos = toDto(expected);
            assertThat(actual).containsExactly(expectedDtos.toArray(new ExpenseDto[]{}));
        }
    }

    private Expense createExpense() {
        int sum = random.nextInt(200);
        String category = "category_" + random.nextInt(10);
        String comment = "comment_" + random.nextInt(10);
        return Expense.builder()
                .createdAt(Instant.now())
                .sum(new BigDecimal(sum))
                .category(category)
                .comment(comment)
                .build();
    }

}