package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.exception.InternalServerError;
import com.paypal.bfs.test.employeeserv.repositories.IRepository;
import com.paypal.bfs.test.employeeserv.response.ResponseObject;
import com.paypal.bfs.test.employeeserv.validation.InputValidation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;


/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

    private final IRepository iRepository;
    public static StringBuilder badRequestResponse = new StringBuilder("Invalid input, ");

    public EmployeeResourceImpl(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        Integer employeeId = Integer.parseInt(id);
        try {
            Employee emp = iRepository.retrive(employeeId);
            if (emp.getId() != null) {
                //return getResponseEntity(Employee.class, emp, getHttpHeaders(), HttpStatus.OK);
                return ResponseEntity.ok(emp);
            } else {
                throw new EmployeeNotFoundException("Employee with employee id :" + id + " not found !");
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
            throw new InternalServerError("Internal Server Error, Please try again later");
        }
    }

    @Override
    public ResponseEntity<Object> addEmployee(@Valid @RequestBody Employee employee) {
        ResponseObject responseObject = new ResponseObject();

        responseObject.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        try {
            if (InputValidation.validateInput(employee)) {
                if (iRepository.persist(employee)) {
                    responseObject.setMessage("Employee got added successfully !");
                    responseObject.setStatus(201);
                    return getResponseEntity(Object.class, responseObject, getHttpHeaders(), HttpStatus.CREATED);
                } else {
                    responseObject.setMessage("Employee already exists !");
                    responseObject.setStatus(200);
                    return getResponseEntity(Object.class, responseObject, getHttpHeaders(), HttpStatus.OK);
                }
            } else {
                throw new BadRequestException(badRequestResponse.toString());
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
            throw new InternalServerError("Internal Server Error, Please try again later");
        } finally {
            badRequestResponse.delete(15, badRequestResponse.length());
        }

    }

    static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    public <T> ResponseEntity<T> getResponseEntity(Class<T> classType, T responseBody, HttpHeaders httpHeaders, HttpStatus httpStatus) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>(responseBody, httpHeaders, httpStatus);
        return responseEntity;
    }

}
