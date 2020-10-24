package com.amairovi.web.backup;

import com.amairovi.back_up.BackUpService;
import com.amairovi.domain.Backup;
import com.amairovi.exception.ServiceException;
import com.amairovi.web.ErrorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Slf4j
public class CreateBackUpHandler implements Handler {
    private final BackUpService backUpService;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(@NotNull final Context ctx) throws JsonProcessingException {
        try {
            final Backup backUp = backUpService.createBackUp();
            final BackupDto dto = new BackupDto(backUp.getFilename(), backUp.getCreatedAt());

            final String result = objectMapper.writeValueAsString(dto);

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
}
