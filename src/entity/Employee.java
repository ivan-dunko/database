package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {
    private final int id;
    private final String lastName;
    private final String firstName;
    private final java.sql.Date birthDate;
    private final int salary;
    private final int experience;
    private final boolean isNative;
    private final int sexId;

    public Employee(ResultSet res) throws SQLException {
        id = res.getInt("id");
        lastName = res.getString("last_name");
        firstName = res.getString("first_name");
        birthDate = res.getDate("birth_date");
        salary = res.getInt("salary");
        experience = res.getInt("experience");
        isNative = res.getBoolean("native");
        sexId = res.getInt("sex_id");
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getBirthDate() {
        return (Date)birthDate.clone();
    }

    public int getSalary() {
        return salary;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isNative() {
        return isNative;
    }

    public int getSexId() {
        return sexId;
    }
}
