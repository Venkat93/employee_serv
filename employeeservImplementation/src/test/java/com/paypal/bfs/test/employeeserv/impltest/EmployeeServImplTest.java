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

import java.sql.SQLException;

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
        Assert.assertEquals(getEmployee().getId(),responseEntity.getBody().getId());
    }

    @Test
    public void employeeGetByIdWithSqlException() throws SQLException, NullPointerException{
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

    /*@Test
    public void addEmployee(){
        employeeResourceImpl.addEmployee()
    }*/




    public Employee getEmployee(){
        Employee emp = new Employee();
        Address address= new Address();
        address.setLine1("3503 Aston manor ct");
        address.setLine2("apt 203");
        address.setCity("Silver Spring");
        address.setState("Maryland");
        address.setCountry("USA");
        address.setZipCode(20904);
        emp.setId(205);
        emp.setFirstName("pooja");
        emp.setLastName("eaga");
        emp.setDateOfBirth("Nov 02 1993");
        emp.setAddress(address);
        return emp;

    }

    static HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }



}
