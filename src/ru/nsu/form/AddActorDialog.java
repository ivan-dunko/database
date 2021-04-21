package ru.nsu.form;

import ru.nsu.IdName;
import ru.nsu.controller.ActorController;
import ru.nsu.controller.EmployeeController;
import ru.nsu.controller.VoiceController;
import ru.nsu.entity.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddActorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Employee> comboBox1;
    private JFormattedTextField formattedTextField1;
    private JComboBox<IdName> comboBox2;
    private final Connection connection;

    public AddActorDialog(Connection connection) {
        this.connection = connection;

        // loading data in combo boxes
        // voice combo box
        ArrayList<IdName> voices = VoiceController.getIdNameList();
        for (IdName voice: voices)
            comboBox2.addItem(voice);

        // employees data
        ArrayList<Employee> employees = EmployeeController.getAllEmployees();
        for (Employee emp : employees)
            comboBox1.addItem(emp);

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
    }

    private void onOK() {
        // validate height
        int height = 0;
        try{
            height = Integer.parseInt(formattedTextField1.getText());
            if (height < 0 || height > 230) {
                JOptionPane.showMessageDialog(this, "Invalid height parameter",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid height parameter",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // add your code here
        Connection conn = this.connection;
        Component actDial = this;
        int empId = ((Employee)(comboBox1.getSelectedItem())).getId();
        int voiceId = ((IdName)(comboBox2.getSelectedItem())).getId();

        int finalHeight = height;
        new SwingWorker<Void, Void>(){
            Connection connection = conn;
            Component parent = actDial;
            int height_ = finalHeight;
            @Override
            protected Void doInBackground() throws Exception {
                try{
                    PreparedStatement stmt = connection.prepareStatement("insert into actors " +
                            "(employee_id, height, voice_id) " +
                            "values (?, ?, ?)");

                    stmt.setInt(1, empId);
                    stmt.setInt(2, finalHeight);
                    stmt.setInt(3, voiceId);

                    stmt.executeUpdate();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ActorController.updateActorTable();
                            JOptionPane.showMessageDialog(actDial, "Actor has been successfully added",
                                    "Actor added", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
                catch (SQLException e){
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        Exception exc = e;
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(actDial, "Error: " + exc.getMessage(), "Error",
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
}
