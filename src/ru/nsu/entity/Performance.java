package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Performance {
    private final int id;
    private final String director;
    private final String play;
    private final String musician;
    private final Timestamp startTime, endTime;

    public Performance(ResultSet res) throws SQLException {
        String play1;
        this.id = res.getInt("id");
        this.director = res.getString("dir_l_name") + " " + res.getString("dir_f_name");
        this.musician = res.getString("mus_l_name") + " " + res.getString("mus_f_name");
        try {
            play1 = res.getString("play_name");
        }
        catch (SQLException e){
            play1 = res.getString("plays.name");
        }
        this.play = play1;
        this.startTime = res.getTimestamp("start_time");
        this.endTime = res.getTimestamp("end_time");
    }

    public Performance(int id, String director, String musician, String play, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.director = director;
        this.musician = musician;
        this.play = play;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getDirector(){
        return director;
    }

    public String getMusician(){
        return musician;
    }

    public String getPlay() {
        return play;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }
}
