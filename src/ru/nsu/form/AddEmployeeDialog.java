package ru.nsu.form;

import ru.nsu.IdName;
import ru.nsu.controller.EmployeeController;
import ru.nsu.controller.SexController;
import ru.nsu.entity.Employee;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    private JComboBox<IdName> comboBox1;
    private final Connection connection;
    private boolean toEdit;
    private Employee empObj = null;

    public AddEmployeeDialog(Connection connection) {
        this.connection = connection;
        this.toEdit = false;

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
        ArrayList<IdName> sexes = SexController.getIdNameList();
        for (IdName x : sexes)
            comboBox1.addItem(x);
        this.setTitle("Add employee");
    }

    public AddEmployeeDialog(Connection connection, Employee toEdit){
        this(connection);
        this.setTitle("Edit employee info");
        this.toEdit = true;
        this.empObj = toEdit;

        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
        textField1.setText(empObj.getLastName());
        textField2.setText(empObj.getFirstName());
        formattedTextField1.setText(format.format(empObj.getBirthDate()));
        textField3.setText(Integer.toString(empObj.getSalary()));
        textField4.setText(Integer.toString(empObj.getExperience()));
        checkBox1.setSelected(empObj.isNative());
        comboBox1.setSelectedIndex(empObj.getSexId() - 1);
    }

    public boolean isToEdit(){
        return toEdit;
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
                    if (!((AddEmployeeDialog)parent).isToEdit()) {
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
                    else{
                        PreparedStatement stmt = connection.prepareStatement("update employees " +
                                "set " +
                                "last_name = ?," +
                                "first_name = ?," +
                                "birth_date = ?," +
                                "salary = ?," +
                                "experience = ?," +
                                "native = ?," +
                                "sex_id = ?" +
                                "where employees.id = ?");

                        SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");

                        stmt.setString(1, textField1.getText());
                        stmt.setString(2, textField2.getText());
                        stmt.setDate(3, new Date(format.parse(formattedTextField1.getText()).getTime()));
                        stmt.setInt(4, Integer.parseInt(textField3.getText()));
                        stmt.setString(5, textField4.getText());
                        stmt.setString(6, checkBox1.isSelected() ? "1" : "0");
                        stmt.setString(7, Integer.toString(comboBox1.getSelectedIndex() + 1));
                        stmt.setString(8, Integer.toString(empObj.getId()));

                        stmt.executeUpdate();

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                EmployeeController.updateEmployeeTable();
                                JOptionPane.showMessageDialog(empDial, "Employee's info has been successfully edited",
                                        "Employee edit", JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                    }
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
