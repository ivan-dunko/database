package ru.nsu.controller;

import ru.nsu.entity.Employee;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EmployeeController {
    private static volatile Connection connection = null;
    private static volatile JTable empTable = null;

    public static void init(Connection connection, JTable employeeTable){
        EmployeeController.connection = connection;
        EmployeeController.empTable = employeeTable;
        updateEmployeeTable();
    }

    public static ArrayList<Employee> getAllEmployees(){
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from employees";
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                emps.add(new Employee(res));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return emps;
    }


    public static void updateEmployeeTable(){
        new SwingWorker<ArrayList<Employee>, Void>() {
            @Override
            protected ArrayList<Employee> doInBackground() throws Exception {
                ArrayList<Employee> emps = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from employees";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        emps.add(new Employee(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return emps;
            }

            @Override
            protected void done() {
                try {
                    empTable.setModel(new EmployeeTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
