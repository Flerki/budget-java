package com.amairovi.config;

import com.amairovi.web.backup.CreateBackUpHandler;
import com.amairovi.web.backup.ListBackUpHandler;
import com.amairovi.web.expenses.CreateExpenseHandler;
import com.amairovi.web.expenses.ListExpenseHandler;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class WebConfiguration {

    private final CreateBackUpHandler createBackUpHandler;
    private final ListBackUpHandler listBackUpHandler;
    private final CreateExpenseHandler createExpenseHandler;
    private final ListExpenseHandler listExpenseHandler;

    public void create() {
        Javalin app = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(7000);
        app.before(ctx -> {
            log.debug("{} {}", ctx.req.getMethod() , ctx.req.getRequestURI());
        });
        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/backups", listBackUpHandler);
        app.post("/backups", createBackUpHandler);
        app.get("/expenses", listExpenseHandler);
        app.post("/expenses", createExpenseHandler);
    }
}
