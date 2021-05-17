package ru.nsu.form;

import ru.nsu.entity.Actor;
import ru.nsu.table.ActorTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CandidatesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table1;

    public CandidatesDialog(ArrayList<Actor> candidates) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        table1.setModel(new ActorTable(candidates));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
