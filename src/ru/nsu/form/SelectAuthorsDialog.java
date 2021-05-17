package ru.nsu.form;

import ru.nsu.IdName;
import ru.nsu.controller.AuthorController;
import ru.nsu.entity.Author;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SelectAuthorsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<IdName> list1;
    private final JList<IdName> toSave;

    public SelectAuthorsDialog(JList<IdName> toSave) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        ArrayList<Author> authors = AuthorController.getAllAuthors();
        ArrayList<IdName> authList = new ArrayList<IdName>();
        for (Author x : authors)
            authList.add(new IdName(x.getId(), x.getFirstName() + " " + x.getLastName()));
        IdName []arr = (IdName[]) authList.toArray(new IdName[authList.size()]);
        list1.setListData(arr);
        this.toSave = toSave;

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
        // add to `toSave` list selected authors
        ArrayList<IdName> sel = new ArrayList<>(list1.getSelectedValuesList());
        DefaultListModel<IdName> model = (DefaultListModel<IdName>)(this.toSave.getModel());
        model.clear();
        for (IdName i : sel){
            model.addElement(i);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
