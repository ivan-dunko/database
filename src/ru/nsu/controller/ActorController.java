package ru.nsu.controller;

import ru.nsu.entity.Actor;
import ru.nsu.entity.Employee;
import ru.nsu.entity.Role;
import ru.nsu.form.CandidatesDialog;
import ru.nsu.table.ActorTable;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.sql.*;
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

    public static ArrayList<Actor> getAllActors(){
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

    public static void showRoleCandidates(Role role){
        final Role finalRole = role;
        new SwingWorker<ArrayList<Actor>, Void>() {
            Role role = finalRole;
            @Override
            protected ArrayList<Actor> doInBackground() throws Exception {
                StringBuilder query = new StringBuilder("select * from actors join employees on " +
                        "actors.employee_id = employees.id where ");
                Date curr = new Date(new java.util.Date().getTime());
                query.append("sex_id = ? AND ");
                query.append("(YEAR(?) - YEAR(birth_date)) BETWEEN ? AND ?;");
                System.out.println(query);
                PreparedStatement stmt = connection.prepareStatement(query.toString());
                stmt.setInt(1, role.getSexId());
                stmt.setDate(2, curr);
                stmt.setInt(3, role.getMinAge());
                stmt.setInt(4, role.getMaxAge());

                System.out.println(stmt.toString());
                ResultSet res = stmt.executeQuery();
                ArrayList<Actor> actors = new ArrayList<>();
                while (res.next())
                    actors.add(new Actor(res));

                return actors;
            }

            @Override
            protected void done() {
                try {
                    //actTable.setModel(new ActorTable(get()));
                    JDialog candDialog = new CandidatesDialog(get());
                    candDialog.pack();
                    candDialog.setVisible(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
