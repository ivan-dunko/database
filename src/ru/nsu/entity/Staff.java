package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff {
    private final int id;
    private final int employeeId;
    private final String firstName;
    private final String lastName;

    public Staff(ResultSet res) throws SQLException {
        id = res.getInt("id");
        employeeId = res.getInt("employee_id");
        firstName = res.getString("first_name");
        lastName = res.getString("last_name");
    }

    public Staff(int id, int employeeId, String lastName, String firstName){
        this.id = id;
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
