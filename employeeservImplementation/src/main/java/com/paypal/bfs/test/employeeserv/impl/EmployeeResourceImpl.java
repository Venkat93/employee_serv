package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.repositories.IRepository;
import com.paypal.bfs.test.employeeserv.validation.InputValidation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.SQLException;


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
                return getResponseEntity(Employee.class, emp, getHttpHeaders(), HttpStatus.OK);
            } else {
                throw new EmployeeNotFoundException("Employee not found !");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            Employee emp = null;
            return getResponseEntity(Employee.class, emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Employee emp = null;
            return getResponseEntity(Employee.class, emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EmployeeNotFoundException enfe) {
            enfe.printStackTrace();
            Employee emp = null;
            return getResponseEntity(Employee.class, emp, getHttpHeaders(), HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<Object> addEmployee(@Valid @RequestBody Employee employee) {
        try {
            if (InputValidation.validateInput(employee)) {
                if (iRepository.persist(employee)) {
                    return getResponseEntity(Object.class,"Employee got added successfully !", getHttpHeaders(), HttpStatus.CREATED);
                } else {
                    return getResponseEntity(Object.class,"Employee already exists !", getHttpHeaders(), HttpStatus.OK);
                }
            } else {
                throw new BadRequestException("Invalid Input");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return getResponseEntity(Object.class,"Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return getResponseEntity(Object.class,"Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException bre) {
            bre.printStackTrace();
            return getResponseEntity(Object.class,badRequestResponse.toString(), getHttpHeaders(), HttpStatus.BAD_REQUEST);
        }finally {
            badRequestResponse.delete(15, badRequestResponse.length());
        }

    }

    static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    public <T> ResponseEntity<T> getResponseEntity(Class<T> classType, T responseBody, HttpHeaders httpHeaders, HttpStatus httpStatus){
        ResponseEntity<T> responseEntity = new ResponseEntity<>(responseBody,httpHeaders,httpStatus);
        return responseEntity;
    }

}
