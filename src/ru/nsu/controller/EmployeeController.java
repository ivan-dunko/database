package ru.nsu.controller;

import ru.nsu.entity.Employee;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
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

    public static void searchEmployees(SearchContext context){
        // CONSTRUCTING QUERY
        StringBuilder query = new StringBuilder("select * from employees where ");
        // last and firs names
        query.append("last_name LIKE '").append(context.getLastName()).append("%' ");
        query.append("AND first_name LIKE '").append(context.getFirstName()).append("%' ");
        // sex
        if (context.isMan() && context.isWoman())
            query.append("AND (sex_id = 1 OR sex_id = 2) ");
        else if (context.isMan())
            query.append("AND sex_id = 1 ");
        else if (context.isWoman())
            query.append("AND sex_id = 2 ");

        //salary
        query.append("AND salary >= ").
                append(Integer.toString(context.getSalaryMin())).
                append(" AND salary <= ").append(Integer.toString(context.getSalaryMax()));
        //born years
        java.sql.Date minDate = java.sql.Date.valueOf(Integer.toString(context.getYearMin()) + "-01-01");
        java.sql.Date maxDate = java.sql.Date.valueOf(Integer.toString(context.getYearMax()) + "-12-31");

        query.append(" AND birth_date BETWEEN '").
                append(minDate.toString()).append("' AND '").append(maxDate.toString()).append("';");

        System.out.println(query.toString());

        String finalQuery = query.toString();
        new SwingWorker<ArrayList<Employee>, Void>() {
            String query = finalQuery;
            @Override
            protected ArrayList<Employee> doInBackground() throws Exception {
                ArrayList<Employee> emps = new ArrayList<>();
                Statement stmt = connection.createStatement();
                ResultSet res = stmt.executeQuery(query);

                while (res.next()) {
                    emps.add(new Employee(res));
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
