package com.amairovi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Error {

    BACK_UP_WAS_NOT_CREATED(1);

    private final int code;
}
