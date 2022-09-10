/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2022.
 */

package com.example.modularmonoliths.common.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
