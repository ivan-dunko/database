package ru.nsu.form;

import ru.nsu.IdName;
import ru.nsu.controller.PlayController;
import ru.nsu.controller.RoleController;
import ru.nsu.entity.Play;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlayChoiceDialog2 extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<IdName> comboBox1;

    public PlayChoiceDialog2() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ArrayList<Play> plays = PlayController.getAllPlays();
        ArrayList<IdName> pIdName = new ArrayList<>();
        for (Play p : plays)
            comboBox1.addItem(new IdName(p.getId(), p.getName()));

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
        // add your code here
        IdName chosen = comboBox1.getItemAt(comboBox1.getSelectedIndex());
        dispose();
        JDialog addPerf = new AddPerformanceDialog(chosen.getId());
        addPerf.pack();
        addPerf.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
