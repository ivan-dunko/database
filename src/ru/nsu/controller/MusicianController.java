package ru.nsu.controller;

import ru.nsu.entity.Director;
import ru.nsu.entity.Musician;
import ru.nsu.entity.Staff;
import ru.nsu.table.MusicianTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MusicianController {
    private static volatile Connection connection = null;
    private static volatile JTable musTable = null;

    public static void init(Connection connection, JTable musTable){
        MusicianController.connection = connection;
        MusicianController.musTable = musTable;
        updateMusicianTable();
    }

    public static ArrayList<Musician> getAllMusicians(){
        ArrayList<Musician> musicians = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from musicians join employees on " +
                    "musicians.employee_id = employees.id";
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                musicians.add(new Musician(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return musicians;
    }


    public static void updateMusicianTable(){
        new SwingWorker<ArrayList<Staff>, Void>() {
            @Override
            protected ArrayList<Staff> doInBackground() throws Exception {
                ArrayList<Staff> dirs = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from musicians join employees on " +
                            "musicians.employee_id = employees.id";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        dirs.add(new Staff(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return dirs;
            }

            @Override
            protected void done() {
                try {
                    musTable.setModel(new MusicianTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
