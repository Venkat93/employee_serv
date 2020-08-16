package com.paypal.bfs.test.employeeserv.validation;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean validateInput(Employee emp) {

        List<String> requiredInputAttributes = Arrays.asList("id", "first_name", "last_name", "line1", "city", "state", "country", "zip_code");
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";

        if (emp.getId() == null || emp.getFirstName() == null || emp.getLastName() == null || emp.getAddress().getLine1() == null || emp.getAddress().getCity() == null || emp.getAddress().getState() == null
                || emp.getAddress().getCountry() == null || emp.getAddress().getZipCode() == null) {
            EmployeeResourceImpl.badRequestResponse.append("You might have missed to send the required attributes :" + requiredInputAttributes);
            return false;
        }
        if (emp.getId().equals("") || emp.getFirstName().equals("") || emp.getLastName().equals("") || emp.getAddress().getLine1().equals("") || emp.getAddress().getCity().equals("")
                || emp.getAddress().getState().equals("") || emp.getAddress().getState().equals("") || emp.getAddress().getCountry().equals("") || emp.getAddress().getZipCode().equals("")) {
            EmployeeResourceImpl.badRequestResponse.append("You might have sent empty value to the required attributes :" + requiredInputAttributes + " Please send the valid values to them");
            return false;
        }
        if (emp.getFirstName().length() > 255 || emp.getLastName().length() > 255 || emp.getAddress().getLine1().length() > 255 || emp.getAddress().getCity().length() > 255
                || emp.getAddress().getState().length() > 255 || emp.getAddress().getCountry().length() > 255) {
            EmployeeResourceImpl.badRequestResponse.append("You might have sent more than 255 characters for the given attributes : [first_name, last_name, line1, city, state, country] , Please send less than 255 characters");
            return false;
        }
        if (emp.getAddress().getLine2() != null) {
            if (emp.getAddress().getLine2().length() > 255) {
                EmployeeResourceImpl.badRequestResponse.append("You might have sent more than 255 characters for line2 attribute, please send less than 255 characters");
                return false;
            }
        }

        //Zip Code Validation with regex
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emp.getAddress().getZipCode().toString());
        if (!matcher.matches()) {
            EmployeeResourceImpl.badRequestResponse.append("Please send the valid zip_code");
            return false;
        }

        //Date of birth validation whether user has provided valid date format or not
        if (emp.getDateOfBirth() != null) {
            if (emp.getDateOfBirth() != "") {
                String dateStr = emp.getDateOfBirth().toString();
                try {
                    LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                } catch (DateTimeParseException e) {
                    EmployeeResourceImpl.badRequestResponse.append("You might have sent date in different format, please provide the date in the given format \'MM-dd-yyyy\' ");
                    return false;
                }
            }
        }
        return true;
    }
}
