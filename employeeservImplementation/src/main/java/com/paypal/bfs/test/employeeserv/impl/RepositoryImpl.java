package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.repositories.IRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class RepositoryImpl implements IRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Override
    public Boolean persist(Employee emp) throws SQLException, NullPointerException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if ((retrive(emp.getId())).getId() == null) {
                con = getConnection();
                pstmt = con.prepareStatement("INSERT INTO employee (id, firstname, lastname, dob, line1, line2, city, state, country, zip) VALUES (?,?,?,?,?,?,?,?,?,?)");
                pstmt.setInt(1, emp.getId());
                pstmt.setString(2, emp.getFirstName());
                pstmt.setString(3, emp.getLastName());
                pstmt.setString(4, emp.getDateOfBirth().toString());
                pstmt.setString(5, emp.getAddress().getLine1());
                pstmt.setString(6, emp.getAddress().getLine2());
                pstmt.setString(7, emp.getAddress().getCity());
                pstmt.setString(8, emp.getAddress().getState());
                pstmt.setString(9, emp.getAddress().getCountry());
                pstmt.setInt(10, emp.getAddress().getZipCode());
                pstmt.execute();
                return true;
            }
            return false;
        } finally {
            if (con != null)
                con.close();
            if (pstmt != null)
                pstmt.close();
        }
    }


    @Override
    public Employee retrive(Integer id) throws SQLException, NullPointerException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Employee emp = new Employee();
        Address address = new Address();
        try {
            con = getConnection();
            pstmt = con.prepareStatement("select * from employee where id = ?");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emp.setId(rs.getInt("id"));
                emp.setFirstName(rs.getString("firstname"));
                emp.setLastName(rs.getString("lastname"));
                emp.setDateOfBirth(rs.getString("dob"));
                address.setLine1(rs.getString("line1"));
                address.setLine2(rs.getString("line2"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setCountry(rs.getString("country"));
                address.setZipCode(rs.getInt("zip"));
                emp.setAddress(address);
            }
            return emp;
        } finally {
            if (con != null)
                con.close();
            if (pstmt != null)
                pstmt.close();
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(driverClassName);
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
