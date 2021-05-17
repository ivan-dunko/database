package ru.nsu.controller;

import ru.nsu.entity.Actor;
import ru.nsu.entity.Director;
import ru.nsu.entity.Play;
import ru.nsu.entity.Role;
import ru.nsu.table.DirectorTable;
import ru.nsu.table.RoleTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class RoleController {
    private static volatile Connection connection = null;
    private static volatile JTable roleTable = null;

    public static void init(Connection connection, JTable roleTable){
        RoleController.connection = connection;
        RoleController.roleTable = roleTable;
        //updatePlaysTable();
    }

    public static ArrayList<Role> getRoles(int playId){
        ArrayList<Role> roles = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from roles where play_id = " + playId;
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                roles.add(new Role(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return roles;
    }

    public static void updateRoleTable(int playId){
        new SwingWorker<ArrayList<Role>, Void>() {
            @Override
            protected ArrayList<Role> doInBackground() throws Exception {
                ArrayList<Role> roles = null;
                try {
                    roles = RoleController.getRoles(playId);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return roles;
            }

            @Override
            protected void done() {
                try {
                    roleTable.setModel(new RoleTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static boolean isCandidate(Role role, Actor actor){
        boolean sex = role.getSexId() == actor.getEmployee().getSexId();

        Date birth = actor.getEmployee().getBirthDate();
        Date curr = new Date(new java.util.Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(curr);
        int currYear = cal.get(Calendar.YEAR);
        cal.setTime(birth);
        int birthYear = cal.get(Calendar.YEAR);

        int age = currYear - birthYear;
        boolean ageIn = age >= role.getMinAge() && age <= role.getMaxAge();

        return ageIn && sex;
    }
}
