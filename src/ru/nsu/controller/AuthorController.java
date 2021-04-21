package ru.nsu.controller;

import ru.nsu.entity.Author;
import ru.nsu.entity.Director;
import ru.nsu.table.AuthorTable;
import ru.nsu.table.DirectorTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AuthorController {
    private static volatile Connection connection = null;
    private static volatile JTable authTable = null;

    public static void init(Connection connection, JTable authTable){
        AuthorController.connection = connection;
        AuthorController.authTable = authTable;
        updateAuthorTable();
    }

    /*
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
    */

    public static void updateAuthorTable(){
        new SwingWorker<ArrayList<Author>, Void>() {
            @Override
            protected ArrayList<Author> doInBackground() throws Exception {
                ArrayList<Author> authors = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from authors";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        authors.add(new Author(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return authors;
            }

            @Override
            protected void done() {
                try {
                    authTable.setModel(new AuthorTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
