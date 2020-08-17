package com.paypal.bfs.test.employeeserv.response;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class ResponseObject {
    private Timestamp timeStamp;
    private int status;
    private String message;


    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "timeStamp=" + timeStamp +
                ", message='" + message + '\'' +
                ", httpStatus=" + status +
                '}';
    }
}
