package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.BadRequestException;
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

    public EmployeeResourceImpl(IRepository iRepository) {
        this.iRepository = iRepository;
    }


    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        Integer employeeId = Integer.parseInt(id);
        try {
            Employee emp = iRepository.retrive(employeeId);
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.OK);
            return responseEntity;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            Employee emp = null;
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Employee emp = null;
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }


    }

    @Override
    public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee) {
        try {
            if (InputValidation.validateInput(employee)) {
                if (iRepository.persist(employee)) {
                    ResponseEntity<String> responseEntity = new ResponseEntity<>("Employee got added successfully !", getHttpHeaders(), HttpStatus.CREATED);
                    return responseEntity;
                } else {
                    ResponseEntity<String> responseEntity = new ResponseEntity<>("Employee already exists !", getHttpHeaders(), HttpStatus.OK);
                    return responseEntity;
                }
            } else {
                throw new BadRequestException("Invalid Input");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } catch (BadRequestException bre) {
            bre.printStackTrace();
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Invalid Input", getHttpHeaders(), HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

    }

    static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
