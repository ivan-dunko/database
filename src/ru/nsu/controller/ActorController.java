package ru.nsu.controller;

import ru.nsu.entity.Actor;
import ru.nsu.entity.Employee;
import ru.nsu.table.ActorTable;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ActorController {
    private static volatile Connection connection = null;
    private static volatile JTable actTable = null;

    public static void init(Connection connection, JTable actorTable){
        ActorController.connection = connection;
        ActorController.actTable = actorTable;
        updateActorTable();
    }

    public static ArrayList<Actor> getAllActors(JTable actTable){
        /*
        if (connection == null)
            return null;

        try {
            Statement stmt = connection.createStatement();
            String query = "select * from employees";
            ResultSet res = stmt.executeQuery(query);

            ArrayList<Employee> emps = new ArrayList<>();
            while (res.next()){
                emps.add(new Employee(res));
            }

            return emps;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
        */

        ArrayList<Actor> acts = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from actors join employees on " +
                    "actors.employee_id = employees.id";
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                acts.add(new Actor(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return acts;
    }


    public static void updateActorTable(){
        new SwingWorker<ArrayList<Actor>, Void>() {
            @Override
            protected ArrayList<Actor> doInBackground() throws Exception {
                ArrayList<Actor> acts = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from actors join employees on " +
                            "actors.employee_id = employees.id";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        acts.add(new Actor(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return acts;
            }

            @Override
            protected void done() {
                try {
                    actTable.setModel(new ActorTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
