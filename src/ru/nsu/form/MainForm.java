package ru.nsu.form;

import ru.nsu.IdName;
import ru.nsu.controller.*;
import ru.nsu.entity.Author;
import ru.nsu.entity.Employee;
import ru.nsu.entity.Role;
import ru.nsu.table.AuthorTable;
import ru.nsu.table.EmployeeTable;
import ru.nsu.table.RoleTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.ArrayList;

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
    private JButton addMusButton;
    private JButton button2;
    private JButton editMusButton;
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
    private JTable perfTable;
    private JTextField textField3;
    private JTextField textField4;
    private JCheckBox musicalBox;
    private JCheckBox comedyBox;
    private JCheckBox dramaBox;
    private JCheckBox melodramaBox;
    private JCheckBox tragedyBox;
    private JScrollPane genrePane;
    private JPanel gPane;
    private JButton searchPerfButton;
    private JButton showPerfButton;
    private JCheckBox vaudevilleBox;
    private JList<IdName> list1;
    private JButton addAuthorsButton;
    private JPanel agesPanel;
    private JCheckBox cent16CheckBox;
    private JCheckBox cent17CheckBox;
    private JCheckBox cent18CheckBox;
    private JCheckBox cent19CheckBox;
    private JCheckBox cent20CheckBox;
    private JPanel datePanel;
    private JCheckBox firstCheckBox;
    private JTable rolesTable;
    private JButton choosePlayButton;
    private JButton candidateButton;
    private JLabel playLabel;
    private JTable table2;
    private JButton addPerfButton;
    private JButton sellTicketButton;
    private JButton proceedsButton;
    private JButton freePlacesButton;
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

        list1.setModel(new DefaultListModel<>());
        //agesPanel.setLayout(new BasicSplitPaneUI.BasicVerticalLayoutManager());
        agesPanel.setLayout(new GridLayout(5, 1));
        //datePanel.setLayout(new GridBagLayout());
        //list1.setPrototypeCellValue(new IdName(-1, "   asdasdas"));
        //list1.setListData(new IdName[]{new IdName(-1, "             ")});

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
            //PlayController.init(connection, rolesTable);
            PlayController.init(connection);
            RoleController.init(connection, rolesTable);
            PerformanceController.init(connection, perfTable);
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
        showPerfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PerformanceController.updatePerformanceTable();
            }
        });
        searchPerfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Timestamp start = Timestamp.valueOf(textField4.getText() + ":00");
                    Timestamp end = Timestamp.valueOf(textField3.getText() + ":00");
                    boolean vaudeville = vaudevilleBox.isSelected();
                    boolean comedy = comedyBox.isSelected();
                    boolean drama = dramaBox.isSelected();
                    boolean melodrama = melodramaBox.isSelected();
                    boolean musical = musicalBox.isSelected();
                    boolean tragedy = tragedyBox.isSelected();
                    boolean first = firstCheckBox.isSelected();
                    boolean cent16 = cent16CheckBox.isSelected();
                    boolean cent17 = cent17CheckBox.isSelected();
                    boolean cent18 = cent18CheckBox.isSelected();
                    boolean cent19 = cent19CheckBox.isSelected();
                    boolean cent20 = cent20CheckBox.isSelected();
                    ArrayList<IdName> authIds = new ArrayList<>(list1.getSelectedValuesList());

                    PerformanceController.SearchContext searchContext = new PerformanceController.SearchContext(
                            start,
                            end,
                            vaudeville,
                            comedy,
                            drama,
                            melodrama,
                            musical,
                            tragedy,
                            first,
                            cent16,
                            cent17,
                            cent18,
                            cent19,
                            cent20,
                            authIds
                    );

                    PerformanceController.searchPerformances(searchContext);
                }
                catch (IllegalArgumentException exc){
                    JOptionPane.showMessageDialog(null, "Неверные данные");
                }
            }
        });
        addAuthorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog authDiag = new SelectAuthorsDialog(list1);
                authDiag.pack();
                authDiag.setVisible(true);
            }
        });
        choosePlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog playDiag = new PlayChoiceDialog(playLabel);
                playDiag.pack();
                playDiag.setVisible(true);
            }
        });
        candidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rolesTable.getSelectedRow();
                if (row == -1)
                    return;
                Role role = ((RoleTable)rolesTable.getModel()).getRoleObject(row);
                ActorController.showRoleCandidates(role);
            }
        });
        addPerfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog chosePlay = new PlayChoiceDialog2();
                chosePlay.pack();
                chosePlay.setVisible(true);
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
