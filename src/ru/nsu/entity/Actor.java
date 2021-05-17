package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Actor {
    private final int id;
    private final int employeeId;
    private final int height;
    private final int voiceId;
    private final String lastName;
    private final String firstName;
    private final Employee employee;

    public Actor(ResultSet res) throws SQLException {
        id = res.getInt("id");
        employeeId = res.getInt("employee_id");
        height = res.getInt("height");
        voiceId = res.getInt("voice_id");
        lastName = res.getString("last_name");
        firstName = res.getString("first_name");
        this.employee = new Employee(res);
    }

    /*
    public Actor(int id, int employeeId, int height, int voiceId, String lastName, String firstName){
        this.id = id;
        this.employeeId = employeeId;
        this.height = height;
        this.voiceId = voiceId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.employee = null;
    }
    */

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public Employee getEmployee(){
        return this.employee;
    }

    public int getHeight() {
        return height;
    }

    public int getVoiceId() {
        return voiceId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString(){
        return this.getLastName() + " " + this.getFirstName();
    }
}
