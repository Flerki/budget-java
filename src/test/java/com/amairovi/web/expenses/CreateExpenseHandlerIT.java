package com.amairovi.web.expenses;

import com.amairovi.domain.Expense;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.amairovi.web.expenses.ExpenseUtils.toDto;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class CreateExpenseHandlerIT extends ITProto {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    void shouldCreateExpenseSuccessfullyWhenAllRequiredFieldsAreProvided() throws IOException {
        assertThat(expenseRepository.findAll()).isEmpty();

        ExpenseDto expenseDto = new ExpenseDto(
                "23.02.2018", "76", "продукты", "окей"
        );

        String json = OBJECT_MAPPER.writeValueAsString(expenseDto);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(HOST + "/expenses")
                .post(body)
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(HttpStatus.CREATED_201);
            String responseBody = Objects.requireNonNull(response.body()).string();
            assertThat(responseBody).isEqualTo(
                    "{" +
                            "\"date\":\"23.02.2018\"," +
                            "\"sum\":\"76\"," +
                            "\"category\":\"продукты\"," +
                            "\"comment\":\"окей\"" +
                            "}"
            );
            List<Expense> actual = expenseRepository.findAll();
            assertThat(actual).hasSize(1);

            ExpenseDto dto = OBJECT_MAPPER.readValue(responseBody, ExpenseDto.class);
            assertThat(toDto(actual)).containsExactly(dto);
        }
    }


}