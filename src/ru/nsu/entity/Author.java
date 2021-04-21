package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Author {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final Date birthDate;
    private final Date deathDate;

    public Author(ResultSet res) throws SQLException {
        id = res.getInt("id");
        firstName = res.getString("first_name");
        lastName = res.getString("last_name");
        birthDate = res.getDate("birth_date");
        deathDate = res.getDate("death_date");
    }

    public Author(int id, int employeeId, String lastName, String firstName, Date birthDate, Date deathDate){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getDeathDate() {
        return deathDate;
    }
}
