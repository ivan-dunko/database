package ru.nsu.controller;

import ru.nsu.entity.Director;
import ru.nsu.table.DirectorTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DirectorController {
    private static volatile Connection connection = null;
    private static volatile JTable dirTable = null;

    public static void init(Connection connection, JTable dirTable){
        DirectorController.connection = connection;
        DirectorController.dirTable = dirTable;
        updateDirectorTable();
    }

    public static ArrayList<Director> getAllDirectors(JTable dirTable){
        ArrayList<Director> dirs = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from actors join employees on " +
                    "actors.employee_id = employees.id";
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                dirs.add(new Director(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return dirs;
    }


    public static void updateDirectorTable(){
        new SwingWorker<ArrayList<Director>, Void>() {
            @Override
            protected ArrayList<Director> doInBackground() throws Exception {
                ArrayList<Director> dirs = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from directors join employees on " +
                            "directors.employee_id = employees.id";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        dirs.add(new Director(res));
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
                    dirTable.setModel(new DirectorTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
