import controller.EmployeeController;
import table.EmployeeTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class Sample {
    private JTabbedPane tabs;
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTable employeesTable;
    private JButton empAddButton;
    private JButton empDelButton;
    private JButton empEditButton;
    private volatile Connection connection = null;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    Sample(Connection connection){
        this.connection = connection;
        EmployeeController.init(connection, employeesTable);

        //employeesTable.setModel(new EmployeeTable(EmployeeController.getAllEmployees()));
        empAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog empDiag = new AddEmployeeDialog(connection);
                empDiag.pack();
                empDiag.setVisible(true);
            }
        });
    }

    public void init(){
        EmployeeController.getAllEmployees(employeesTable);
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
                Sample sample = new Sample(finalConnection);
                sample.init();
                frame.setContentPane(sample.panel1);
                frame.setVisible(true);
                frame.pack();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        });

        System.out.println(SwingUtilities.isEventDispatchThread());
    }


}
