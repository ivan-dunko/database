package ru.nsu.form;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import ru.nsu.controller.*;
import ru.nsu.entity.Author;
import ru.nsu.entity.Employee;
import ru.nsu.table.AuthorTable;
import ru.nsu.table.EmployeeTable;

import javax.swing.*;
import java.awt.*;
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
    private JButton empDeleteButton;
    private JSplitPane empSplitPane;
    private JTextField textField1;
    private JTextField textField2;
    private JCheckBox manCheckBox;
    private JCheckBox womanCheckBox;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;
    private JButton searchButton;
    private JButton showAllButton;
    private JTable table1;
    private JTextField textField3;
    private JTextField textField4;
    private JCheckBox мюзиклCheckBox;
    private JCheckBox комедияCheckBox;
    private JCheckBox драмаCheckBox;
    private JCheckBox мелодрамаCheckBox;
    private JCheckBox трагедияCheckBox;
    private JScrollPane genrePane;
    private JPanel gPane;
    private JButton поискButton;
    private JButton показатьВсеButton;
    private volatile Connection connection = null;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    MainForm(Connection connection, JFrame frame){
        this.connection = connection;

        spinner3.setValue(1900);
        spinner4.setValue(2020);
        spinner1.setValue(0);
        spinner2.setValue(100);

        //genrePane = new JScrollPane(gPane);

        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try {
            EmployeeController.init(connection, employeesTable);
            SexController.init(connection);
            VoiceController.init(connection);
            ActorController.init(connection, actorsTable);
            AuthorController.init(connection, dirTable);
            MusicianController.init(connection, musTable);
            DirectorController.init(connection, dirTable);
            StaffController.init(connection, staffTable);
            AuthorController.init(connection, authorTable);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        empSplitPane.setDividerLocation(0.8 * frame.getWidth());

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
                int ind = authorTable.getSelectedRow();
                if (ind == -1)
                    return;

                Author author = ((AuthorTable)authorTable.getModel()).getAuthorObj(ind);
                JDialog diag = new ShowPlaysDialog(author, connection);
                diag.pack();
                diag.setVisible(true);
            }
        });
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeController.updateEmployeeTable();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName, lastName;
                lastName = textField1.getText();
                firstName = textField2.getText();
                boolean man, woman;
                man = manCheckBox.isSelected();
                woman = womanCheckBox.isSelected();
                int minSalary = 0, maxSalary = 0;
                try{
                    minSalary = (int) spinner1.getValue();
                    maxSalary = (int) spinner2.getValue();
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    return;
                }

                int yearMin, yearMax;
                try{
                    yearMin = (int) spinner3.getValue();
                    yearMax = (int) spinner4.getValue();
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    return;
                }

                EmployeeController.searchEmployees(new EmployeeController.SearchContext(
                        firstName,
                        lastName,
                        man,
                        woman,
                        minSalary,
                        maxSalary,
                        yearMin,
                        yearMax
                ));
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
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                MainForm mainForm = new MainForm(finalConnection, frame);
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
