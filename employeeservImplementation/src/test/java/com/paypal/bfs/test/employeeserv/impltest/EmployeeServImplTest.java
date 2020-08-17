package com.paypal.bfs.test.employeeserv.impltest;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.InternalServerError;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import org.springframework.beans.factory.annotation.Autowired;
import com.paypal.bfs.test.employeeserv.repositories.IRepository;

import javax.validation.constraints.Null;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServImplTest {

    @InjectMocks
    EmployeeResourceImpl employeeResourceImpl;

    @Mock
    IRepository iRepository;

    @Test
    public void employeeGetById() throws SQLException, NullPointerException {
        Mockito.doReturn(getEmployee()).when(iRepository).retrive(Mockito.anyInt());
        ResponseEntity<Employee> responseEntity = employeeResourceImpl.employeeGetById("205");
        Assert.assertEquals(getEmployee().getId(), responseEntity.getBody().getId());
    }

    @Test(expected = InternalServerError.class)
    public void employeeGetByIdWithSqlException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).retrive(Mockito.anyInt());
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(null, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(responseEntity, employeeResourceImpl.employeeGetById("205"));
    }

    @Test(expected = InternalServerError.class)
    public void employeeGetByIdWithNullPointerException() throws NullPointerException, SQLException {
        Mockito.doThrow(new NullPointerException()).when(iRepository).retrive(Mockito.anyInt());
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(null, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(responseEntity, employeeResourceImpl.employeeGetById("205"));
    }

    @Test
    public void addEmployee() throws SQLException {
        Mockito.doReturn(true).when(iRepository).persist(Mockito.any());
        Assert.assertEquals(HttpStatus.CREATED, employeeResourceImpl.addEmployee(getEmployee()).getStatusCode());
    }

    @Test
    public void addEmployeeFalseCase() throws SQLException, NullPointerException {
        Mockito.doReturn(false).when(iRepository).persist(Mockito.any());
        Assert.assertEquals(HttpStatus.OK, employeeResourceImpl.addEmployee(getEmployee()).getStatusCode());
    }

    @Test(expected = InternalServerError.class)
    public void addEmployeeWithSqlException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Internal Server Error", employeeResourceImpl.addEmployee(getEmployee()).getBody());
    }

    @Test(expected = InternalServerError.class)
    public void addEmployeeWithNullPointerException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Internal Server Error", employeeResourceImpl.addEmployee(getEmployee()).getBody());
    }


    public Map<String, Object> getEmployeeRequestObj() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", "205");
        requestMap.put("firstName", "Venkata");
        requestMap.put("lastName", "vanukuri");
        requestMap.put("date_of_birth", "11-02-1992");
        requestMap.put("address", getAddress());

        return requestMap;
    }

    public Address getAddress() {
        Address address = new Address();
        address.setLine1("380 Vista Court dr");
        address.setLine2("apt 2311");
        address.setCity("Plano");
        address.setState("TX");
        address.setCountry("USA");
        address.setZipCode(75074);
        return address;
    }


    public Employee getEmployee() {
        Employee emp = new Employee();
        emp.setId(205);
        emp.setFirstName("venkata");
        emp.setLastName("vanukuri");
        emp.setDateOfBirth("11-02-1992");
        emp.setAddress(getAddress());
        return emp;

    }

    static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }


}
