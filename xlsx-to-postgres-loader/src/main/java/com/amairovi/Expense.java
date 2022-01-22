package com.amairovi;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
public class Expense {
    private Instant createdAt;
    private BigDecimal sum;
    private String category;
    private String comment;
}
