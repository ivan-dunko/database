import controller.EmployeeController;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class AddEmployeeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JFormattedTextField formattedTextField1;
    private JTextField textField3;
    private JTextField textField4;
    private JCheckBox checkBox1;
    private JComboBox<String> comboBox1;
    private final Connection connection;

    public AddEmployeeDialog(Connection connection) {
        this.connection = connection;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        formattedTextField1.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter()));
        comboBox1.addItem("М");
        comboBox1.addItem("Ж");
        this.setTitle("Add employee");
    }

    private void onOK() {
        // add your code here
        Connection conn = this.connection;
        Component empDial = this;
        new SwingWorker<Void, Void>(){

            Connection connection = conn;
            Component parent = empDial;
            @Override
            protected Void doInBackground(){
                try{
                    PreparedStatement stmt = connection.prepareStatement("insert into employees " +
                            "(last_name, first_name, birth_date, salary, experience, native, sex_id) " +
                            "values (?, ?, ?, ?, ?, ?, ?)");

                    SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
                    stmt.setString(1, textField1.getText());
                    stmt.setString(2, textField2.getText());
                    stmt.setDate(3, new Date(format.parse(formattedTextField1.getText()).getTime()));
                    stmt.setInt(4, Integer.parseInt(textField3.getText()));
                    stmt.setString(5, textField4.getText());
                    stmt.setString(6, checkBox1.isSelected() ? "1" : "0");
                    stmt.setString(7, Integer.toString(comboBox1.getSelectedIndex() + 1));

                    stmt.executeUpdate();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            EmployeeController.updateEmployeeTable();
                            JOptionPane.showMessageDialog(empDial, "Employee has been successfully added",
                                    "Employee added", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        Exception exc = e;
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(empDial, "Error: " + exc.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });

                }

                return null;
            }
        }.execute();

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*
    public static void main(String[] args) {
        AddEmployeeDialog dialog = new AddEmployeeDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
     */
}
