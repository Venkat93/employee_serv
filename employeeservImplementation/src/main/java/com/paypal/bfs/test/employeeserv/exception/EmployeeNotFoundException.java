package com.paypal.bfs.test.employeeserv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String exception) {
        super(exception);
    }
}
