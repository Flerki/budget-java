package com.amairovi.config;

import com.amairovi.back_up.BackUpService;
import io.javalin.Javalin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;

@RequiredArgsConstructor
@Slf4j
public class WebConfiguration {

    private final BackUpService backUpService;

    public void create() {
        Javalin app = Javalin.create().start(7000);
        app.before(ctx -> {
            log.debug("{}", ctx.req.getRequestURI());
        });
        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/back-ups", ctx -> {
            backUpService.createBackUp();
            ctx.status(HttpStatus.CREATED_201);
        });
    }
}
