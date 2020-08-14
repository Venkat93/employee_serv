package com.paypal.bfs.test.employeeserv.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.repositories.IRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

    private final IRepository iRepository;

    public EmployeeResourceImpl(IRepository iRepository){
        this.iRepository = iRepository;
    }


    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        Long employeeId = Long.parseLong (id);
        try {
            Employee emp = iRepository.retrive(employeeId);
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.OK);
            return responseEntity;
        }catch(SQLException sqle){
            sqle.printStackTrace();
            Employee emp = null;
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }catch(NullPointerException npe){
            npe.printStackTrace();
            Employee emp = null;
            ResponseEntity<Employee> responseEntity = new ResponseEntity<>(emp, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }


    }

    @Override
    public ResponseEntity<String> addEmployee(@RequestBody Map<String,Object> employeeMap){
        ObjectMapper oMapper = new ObjectMapper();
        Employee employee = new Employee();
        employee.setId(Integer.valueOf(employeeMap.get("id").toString()));
        employee.setFirstName(employeeMap.get("firstName").toString());
        employee.setLastName(employeeMap.get("lastName").toString());
        employee.setDateOfBirth(employeeMap.get("date_of_birth").toString());
        Object obj = employeeMap.get("address");
        Address address = oMapper.convertValue(obj, Address.class);
        employee.setAddress(address);
        try {
            if (iRepository.persist(employee)){
                ResponseEntity<String> responseEntity = new ResponseEntity<>("Employee got added successfully !", getHttpHeaders(), HttpStatus.CREATED);
                return responseEntity;
            }else{
                ResponseEntity<String> responseEntity = new ResponseEntity<>("Employee already exists !", getHttpHeaders(), HttpStatus.OK);
                return responseEntity;
            }
        }catch(SQLException sqle){
            sqle.printStackTrace();
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }catch(NullPointerException npe){
            npe.printStackTrace();
            ResponseEntity<String> responseEntity = new ResponseEntity<>("Internal Server Error", getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        }

    }

    static HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }


}
