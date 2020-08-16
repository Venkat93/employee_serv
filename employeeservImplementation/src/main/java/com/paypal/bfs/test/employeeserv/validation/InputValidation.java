package com.paypal.bfs.test.employeeserv.validation;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

public class InputValidation {
    public static boolean validateInput(Employee emp) {
        if (emp.getId() == null || emp.getFirstName() == null || emp.getLastName() == null || emp.getAddress().getLine1() == null || emp.getAddress().getCity() == null || emp.getAddress().getState() == null
                || emp.getAddress().getCountry() == null || emp.getAddress().getZipCode() == null)
            return false;
        if (emp.getId().equals("") || emp.getFirstName().equals("") || emp.getLastName().equals("") || emp.getAddress().getLine1().equals("") || emp.getAddress().getCity().equals("")
                || emp.getAddress().getState().equals("") || emp.getAddress().getState().equals("") || emp.getAddress().getCountry().equals("") || emp.getAddress().getZipCode().equals(""))
            return false;
        if (emp.getFirstName().length() > 255 || emp.getLastName().length() > 255 || emp.getAddress().getLine1().length() > 255 || emp.getAddress().getCity().length() > 255
                || emp.getAddress().getState().length() > 255 || emp.getAddress().getCountry().length() > 255)
            return false;
        if (emp.getAddress().getLine2() != null) {
            if (emp.getAddress().getLine2().length() > 255)
                return false;
        }
        return true;
    }
}
