package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Play {
    private final int id;
    private final String name;
    private final int year;
    private final String author;
    private final int genreId;
    private final int ageCatId;

    public Play(ResultSet res) throws SQLException {
        id = res.getInt("id");
        name = res.getString("name");
        year = res.getInt("year");
        author = res.getString("last_name") + " " + res.getString("first_name");
        genreId = res.getInt("genre_id");
        ageCatId = res.getInt("category_id");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public int getGenreId() {
        return genreId;
    }

    public int getAgeCatId() {
        return ageCatId;
    }
}
