package com.amairovi.config;

import com.amairovi.web.backup.CreateBackUpHandler;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class WebConfiguration {

    private final CreateBackUpHandler createBackUpHandler;

    public void create() {
        Javalin app = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(7000);
        app.before(ctx -> {
            log.debug("{}", ctx.req.getRequestURI());
        });
        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/backups", createBackUpHandler);
    }
}
