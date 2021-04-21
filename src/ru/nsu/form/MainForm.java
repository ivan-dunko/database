package ru.nsu.form;

import ru.nsu.controller.*;
import ru.nsu.entity.Employee;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainForm {
    private JTabbedPane tabs;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTable employeesTable;
    private JButton empAddButton;
    private JButton empDelButton;
    private JButton empEditButton;
    private JTable actorsTable;
    private JButton actorAddButton;
    private JButton actorDeleteButton;
    private JButton actorEditButton;
    private JTable dirTable;
    private JButton dirAddButton;
    private JButton dirEditButton;
    private JButton dirDeleteButton;
    private JTable musTable;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTable staffTable;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JTable authorTable;
    private JButton addAuthorButton;
    private JButton showPlaysButton;
    private JButton editAuthorButton;
    private JButton deleteAuthorButton;
    private volatile Connection connection = null;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    MainForm(Connection connection){
        this.connection = connection;
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try {
            EmployeeController.init(connection, employeesTable);
            SexController.init(connection);
            VoiceController.init(connection);
            ActorController.init(connection, actorsTable);
            AuthorController.init(connection, dirTable);
            MusicianController.init(connection, musTable);
            StaffController.init(connection, staffTable);
            AuthorController.init(connection, authorTable);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //employeesTable.setModel(new EmployeeTable(EmployeeController.getAllEmployees()));
        empAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog empDiag = new AddEmployeeDialog(connection);
                empDiag.pack();
                empDiag.setVisible(true);
            }
        });
        empEditButton.addActionListener(new ActionListener() {
            JTable employeeTable = employeesTable;
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = employeeTable.getSelectedRow();
                if (row == -1)
                    return;

                Employee emp = ((EmployeeTable)employeeTable.getModel()).getEmployeeObj(row);
                System.out.println(emp);
                JDialog empDiag = new AddEmployeeDialog(connection, emp);
                empDiag.pack();
                empDiag.setVisible(true);
            }
        });
        actorAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addDialog = new AddActorDialog(connection);
                addDialog.pack();
                addDialog.setVisible(true);
            }
        });
        showPlaysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void init(){
        //EmployeeController.getAllEmployees(employeesTable);
    }

    public static void main(String[] args){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                    "root", "1234");
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        Connection finalConnection = connection;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("database");
                MainForm mainForm = new MainForm(finalConnection);
                mainForm.init();
                frame.setContentPane(mainForm.panel1);
                frame.setVisible(true);
                frame.pack();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        });

        System.out.println(SwingUtilities.isEventDispatchThread());
    }


}
