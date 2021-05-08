package ru.nsu.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Performance {
    private final int id;
    private final String stageDirector;
    private final String prodDirector;
    private final String play;
    private final Timestamp startTime, endTime;

    public Performance(ResultSet res) throws SQLException {
        this.id = res.getInt("id");
        this.stageDirector = res.getString("stage_dir_name");
        this.prodDirector = res.getString("prod_dir_name");
        this.play = res.getString("play_name");
        this.startTime = res.getTimestamp("start_time");
        this.endTime = res.getTimestamp("end_time");
    }

    public Performance(int id, String stageDirector, String prodDirector, String play, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.stageDirector = stageDirector;
        this.prodDirector = prodDirector;
        this.play = play;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getStageDirector() {
        return stageDirector;
    }

    public String getProdDirector() {
        return prodDirector;
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
