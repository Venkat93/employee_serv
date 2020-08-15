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
        //try {
        Connection con = getConnection();

        PreparedStatement pstmt1 = con.prepareStatement("select * from employee where id = ? and firstname = ? and lastname = ? and dob=? and line1=? and line2=?" +
                "and city=? and state=? and country=? and zip=?");
        pstmt1.setLong(1, emp.getId());
        pstmt1.setString(2, emp.getFirstName());
        pstmt1.setString(3, emp.getLastName());
        pstmt1.setString(4, emp.getDateOfBirth().toString());
        pstmt1.setString(5, emp.getAddress().getLine1());
        pstmt1.setString(6, emp.getAddress().getLine2());
        pstmt1.setString(7, emp.getAddress().getCity());
        pstmt1.setString(8, emp.getAddress().getState());
        pstmt1.setString(9, emp.getAddress().getCountry());
        pstmt1.setInt(10, emp.getAddress().getZipCode());

        ResultSet rs1 = pstmt1.executeQuery();
        if (!rs1.next()) {

            PreparedStatement pstmt = con.prepareStatement("INSERT INTO employee (id, firstname, lastname, dob, line1, line2, city, state, country, zip) VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setLong(1, emp.getId());
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
            pstmt.close();
            con.close();
            return true;
        }
        pstmt1.close();
        con.close();
        return false;
        /*} catch (Exception e) {
        System.out.println(e.getMessage());
        return "Internal server error";
    }*/
    }


    @Override
    public Employee retrive(Long id) throws SQLException, NullPointerException {
        //try {
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement("select * from employee where id = ?");
        pstmt.setLong(1, id);
        ResultSet rs = pstmt.executeQuery();
        Employee emp = new Employee();
        Address address = new Address();
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
//        }catch (NullPointerException npe) {
//            npe.printStackTrace();
//            return null;
//        }
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
