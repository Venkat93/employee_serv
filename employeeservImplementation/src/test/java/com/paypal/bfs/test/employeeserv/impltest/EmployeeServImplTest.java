package com.paypal.bfs.test.employeeserv.impltest;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
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
    public void employeeGetById() throws SQLException {
        Mockito.doReturn(getEmployee()).when(iRepository).retrive(Mockito.anyLong());
        ResponseEntity<Employee> responseEntity = employeeResourceImpl.employeeGetById("205");
        Assert.assertEquals(getEmployee().getId(), responseEntity.getBody().getId());
    }

    @Test
    public void employeeGetByIdWithSqlException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).retrive(Mockito.anyLong());
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(null, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(responseEntity, employeeResourceImpl.employeeGetById("205"));
    }

    @Test
    public void employeeGetByIdWithNullPointerException() throws NullPointerException, SQLException {
        Mockito.doThrow(new NullPointerException()).when(iRepository).retrive(Mockito.anyLong());
        ResponseEntity<Employee> responseEntity = new ResponseEntity<>(null, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(responseEntity, employeeResourceImpl.employeeGetById("205"));
    }

    @Test
    public void addEmployee() throws SQLException {
        Mockito.doReturn(true).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Employee got added successfully !", employeeResourceImpl.addEmployee(getEmployeeRequestObj()).getBody());
    }

    @Test
    public void addEmployeeFalseCase() throws SQLException, NullPointerException {
        Mockito.doReturn(false).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Employee already exists !", employeeResourceImpl.addEmployee(getEmployeeRequestObj()).getBody());
    }

    @Test
    public void addEmployeeWithSqlException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Internal Server Error", employeeResourceImpl.addEmployee(getEmployeeRequestObj()).getBody());
    }

    @Test
    public void addEmployeeWithNullPointerException() throws SQLException, NullPointerException {
        Mockito.doThrow(new SQLException()).when(iRepository).persist(Mockito.any());
        Assert.assertEquals("Internal Server Error", employeeResourceImpl.addEmployee(getEmployeeRequestObj()).getBody());
    }


    public Map<String, Object> getEmployeeRequestObj() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", "205");
        requestMap.put("firstName", "pooja");
        requestMap.put("lastName", "eaga");
        requestMap.put("date_of_birth", "Nov 02 1993");
        requestMap.put("address", getAddress());

        return requestMap;
    }

    public Address getAddress() {
        Address address = new Address();
        address.setLine1("3503 Aston Manor ct");
        address.setLine2("apt 203");
        address.setCity("Silver Spring");
        address.setState("MD");
        address.setCountry("USA");
        address.setZipCode(20904);
        return address;
    }


    public Employee getEmployee() {
        Employee emp = new Employee();
        emp.setId(205);
        emp.setFirstName("pooja");
        emp.setLastName("eaga");
        emp.setDateOfBirth("Nov 02 1993");
        emp.setAddress(getAddress());
        return emp;

    }

    static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }


}
