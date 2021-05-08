package ru.nsu.controller;

import ru.nsu.entity.Employee;
import ru.nsu.entity.Performance;
import ru.nsu.table.EmployeeTable;
import ru.nsu.table.PerformanceTable;
import sun.misc.Perf;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PerformanceController {
    private static volatile Connection connection = null;
    private static volatile JTable perfTable = null;

    public static void init(Connection connection, JTable employeeTable){
        PerformanceController.connection = connection;
        PerformanceController.perfTable = employeeTable;
        updatePerformanceTable();
    }

    public static ArrayList<Employee> getAllEmployees(){
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from performances";
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


    public static void updatePerformanceTable(){
        new SwingWorker<ArrayList<Performance>, Void>() {
            @Override
            protected ArrayList<Performance> doInBackground() throws Exception {
                ArrayList<Performance> perfs = new ArrayList<>();
                try {
                    Statement stmt = connection.createStatement();
                    String query = "select * from performances";
                    ResultSet res = stmt.executeQuery(query);

                    while (res.next()) {
                        perfs.add(new Performance(res));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return perfs;
            }

            @Override
            protected void done() {
                try {
                    perfTable.setModel(new PerformanceTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static void searchPerformances(EmployeeController.SearchContext context){
        // CONSTRUCTING QUERY
        StringBuilder query = new StringBuilder("select * from performances where ");


        System.out.println(query.toString());

        String finalQuery = query.toString();
        new SwingWorker<ArrayList<Performance>, Void>() {
            String query = finalQuery;
            @Override
            protected ArrayList<Performance> doInBackground() throws Exception {
                ArrayList<Performance> perfs = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet res = stmt.executeQuery(query);

                while (res.next()) {
                    perfs.add(new Performance(res));
                }

                return perfs;
            }

            @Override
            protected void done() {
                try {
                    perfTable.setModel(new PerformanceTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static class SearchContext{
        private final String firstName, lastName;
        private final boolean man, woman;
        private final int salaryMin, salaryMax;
        private final int yearMin, yearMax;

        public SearchContext(
                String firstName,
                String lastName,
                boolean man,
                boolean woman,
                int salaryMin,
                int salaryMax,
                int yearMin,
                int yearMax){
            this.firstName = firstName;
            this.lastName = lastName;
            this.man = man;
            this.woman = woman;
            this.salaryMin = salaryMin;
            this.salaryMax = salaryMax;
            this.yearMin = yearMin;
            this.yearMax = yearMax;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public boolean isMan() {
            return man;
        }

        public boolean isWoman() {
            return woman;
        }

        public int getSalaryMin() {
            return salaryMin;
        }

        public int getSalaryMax() {
            return salaryMax;
        }

        public int getYearMin() {
            return yearMin;
        }

        public int getYearMax() {
            return yearMax;
        }
    }
}
