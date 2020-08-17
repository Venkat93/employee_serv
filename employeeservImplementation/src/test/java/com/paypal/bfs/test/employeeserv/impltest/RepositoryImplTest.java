package com.paypal.bfs.test.employeeserv.impltest;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.impl.RepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/*import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {

    private Random rand = new Random();
    private int upperbound = 999;
    private int int_random = rand.nextInt(upperbound);

    @InjectMocks
    @Spy
    private RepositoryImpl repositoryImpl = new RepositoryImpl();

    @Mock
    Connection connection;

    @Mock
    PreparedStatement preparedStatement;

    @Mock
    ResultSet resultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(repositoryImpl, "url", "jdbc:h2:~/test");
        ReflectionTestUtils.setField(repositoryImpl, "driverClassName", "org.h2.Driver");
        ReflectionTestUtils.setField(repositoryImpl, "username", "sa");
        ReflectionTestUtils.setField(repositoryImpl, "password", "");
    }

    @Test
    public void persist() throws SQLException, NullPointerException {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        RepositoryImpl repositoryImpl = Mockito.spy(RepositoryImpl.class);
        Mockito.doReturn(connection).when(repositoryImpl).getConnection();
        Mockito.doReturn(preparedStatement).when(connection).prepareStatement(Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.doReturn(false).when(resultSet).next();
        Mockito.doReturn(true).when(preparedStatement).execute();
        Mockito.doNothing().when(preparedStatement).close();
        Assert.assertEquals(true, repositoryImpl.persist(getEmployee()));
    }

    @Test
    public void retrive() throws SQLException, NullPointerException {
        Mockito.doReturn(connection).when(repositoryImpl).getConnection();
        Mockito.doReturn(preparedStatement).when(connection).prepareStatement(Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setLong(Mockito.anyInt(), Mockito.anyLong());
        Mockito.doReturn(resultSet).when(preparedStatement).executeQuery();
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt("id")).thenReturn(205);
        Mockito.when(resultSet.getString("firstname")).thenReturn("venkata");
        Mockito.when(resultSet.getString("lastname")).thenReturn("vanukuri");
        Mockito.when(resultSet.getString("dob")).thenReturn("Nov 02 1992");
        Mockito.when(resultSet.getString("line1")).thenReturn("380 Vista Court dr");
        Mockito.when(resultSet.getString("line2")).thenReturn("apt 2311");
        Mockito.when(resultSet.getString("city")).thenReturn("Plano");
        Mockito.when(resultSet.getString("state")).thenReturn("TX");
        Mockito.when(resultSet.getString("country")).thenReturn("USA");
        Mockito.when(resultSet.getInt("zip")).thenReturn(75074);

        Assert.assertEquals(getEmployee().getId(), repositoryImpl.retrive(205).getId());
    }

    @Ignore
    @Test
    public void persistToDB() throws SQLException, NullPointerException{
        repositoryImpl.persist(getEmployee());
    }

    @Ignore
    @Test
    public void retriveFromDB() throws SQLException, NullPointerException{
        repositoryImpl.retrive(int_random).getId();
    }

    @Test
    public void getConnection(){
        repositoryImpl.getConnection();
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
        //emp.setId(int_random);
        emp.setFirstName("venkata");
        emp.setLastName("vanukuri");
        emp.setDateOfBirth("Nov 02 1992");
        emp.setAddress(getAddress());
        return emp;

    }


}
