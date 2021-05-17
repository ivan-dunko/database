package ru.nsu.controller;

import ru.nsu.entity.Director;
import ru.nsu.entity.Play;
import ru.nsu.entity.Staff;
import ru.nsu.table.MusicianTable;
import ru.nsu.table.PlayTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlayController {
    private static volatile Connection connection = null;
    private static volatile JTable playTable = null;

    public static void init(Connection connection){
        PlayController.connection = connection;
        //updatePlaysTable();
    }

    public static ArrayList<Play> getAllPlays(){
        ArrayList<Play> plays = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from plays join authors on plays.author_id = authors.id";
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                plays.add(new Play(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return plays;
    }

    /*
    public static void updatePlaysTable(){
        new SwingWorker<ArrayList<Play>, Void>() {
            @Override
            protected ArrayList<Play> doInBackground() throws Exception {
                ArrayList<Play> plays = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from plays join authors on plays.author_id = authors.id";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        plays.add(new Play(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return plays;
            }

            @Override
            protected void done() {
                try {
                    playTable.setModel(new PlayTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
     */
}
