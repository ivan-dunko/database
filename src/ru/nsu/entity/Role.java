package ru.nsu.entity;

import ru.nsu.controller.SexController;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Role {
    private final int id;
    private final String name;
    private final int sexId;
    private final int minAge;
    private final int maxAge;

    public Role(int id, String name, int sexId, int minAge, int maxAge) {
        this.id = id;
        this.name = name;
        this.sexId = sexId;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public Role(ResultSet res) throws SQLException {
        this.id = res.getInt("id");
        this.name = res.getString("name");
        this.sexId = res.getInt("sex_id");
        this.minAge = res.getInt("min_age");
        this.maxAge = res.getInt("max_age");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSexId() {
        return sexId;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }
}
