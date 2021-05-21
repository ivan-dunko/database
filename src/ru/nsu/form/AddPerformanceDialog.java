package ru.nsu.form;

import jdk.nashorn.internal.scripts.JO;
import ru.nsu.IdName;
import ru.nsu.controller.*;
import ru.nsu.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddPerformanceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JComboBox<Director> dirsComboBox;
    private JComboBox<Musician> musComboBox;
    private JPanel actorsPanel;
    private ArrayList<JComboBox<Actor>> actorBoxes, stuntBoxes;
    private ArrayList<Label> roleLabels;
    private ArrayList<Role> roles;
    private final int playId;

    public AddPerformanceDialog(int playId) {
        this.playId = playId;
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

        ArrayList<Director> dirs = DirectorController.getAllDirectors();
        for (Director dir : dirs)
            dirsComboBox.addItem(dir);

        ArrayList<Musician> musicians = MusicianController.getAllMusicians();
        for (Musician mus : musicians)
            musComboBox.addItem(mus);

        //actorsPanel.setLayout(new GridLayout());
        // TODO
        // 1. Get all roles
        // 2. Get all actors
        // 3. Put stunts and actors
        roles = RoleController.getRoles(playId);
        ArrayList<Actor> actors = ActorController.getAllActors();

        actorsPanel.setLayout(new GridLayout(roles.size() + 1, 3));
        actorsPanel.add(new JLabel("Роль"));
        actorsPanel.add(new JLabel("Актёр"));
        actorsPanel.add(new JLabel("Дублёр"));

        Actor[] actArr = actors.toArray(new Actor[actors.size()]);
        actorBoxes = new ArrayList<>();
        stuntBoxes = new ArrayList<>();
        for (int i = 0; i < roles.size(); ++i){
            JComboBox<Actor> actBox = new JComboBox<>();
            for (Actor actor: actors)
                if (RoleController.isCandidate(roles.get(i), actor))
                    actBox.addItem(actor);
            actorBoxes.add(actBox);

            JComboBox<Actor> stuntBox = new JComboBox<>();
            for (Actor stunt: actors)
                if (RoleController.isCandidate(roles.get(i), stunt))
                    stuntBox.addItem(stunt);
            stuntBoxes.add(stuntBox);
        }

        for (int i = 0; i < roles.size(); ++i){
            actorsPanel.add(new JLabel(roles.get(i).getName()));
            actorsPanel.add(actorBoxes.get(i));
            actorsPanel.add(stuntBoxes.get(i));
        }
    }

    private void onOK() {
        // 1. Check that all actors and candidates
        boolean allCand = true;
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<Actor> stunts = new ArrayList<>();

        for (int i = 0; i < roles.size(); ++i){
            JComboBox<Actor> box = actorBoxes.get(i);
            actors.add((Actor) box.getSelectedItem());
            allCand &= RoleController.isCandidate(roles.get(i),box.getItemAt(box.getSelectedIndex()));

            box = stuntBoxes.get(i);
            stunts.add((Actor) box.getSelectedItem());
            allCand &= RoleController.isCandidate(roles.get(i),box.getItemAt(box.getSelectedIndex()));
        }

        if (!allCand){
            JOptionPane.showMessageDialog(null, "Не все актёры подходят на роль");
            return;
        }

        // 2. Check if all stunts and actors are different
        HashMap<Integer, Integer> idOcurr = new HashMap<>();
        for (int i = 0; i < roles.size(); ++i){
            Actor actor = (Actor)actorBoxes.get(i).getSelectedItem();
            Actor stunt = (Actor)stuntBoxes.get(i).getSelectedItem();

            idOcurr.putIfAbsent(actor.getId(), 0);
            idOcurr.compute(actor.getId(), (k, v)->(v + 1));
            idOcurr.putIfAbsent(stunt.getId(), 0);
            idOcurr.compute(stunt.getId(), (k, v)->(v + 1));
        }

        boolean single = true;
        for (Integer key : idOcurr.keySet())
            single &= idOcurr.get(key) == 1;

        if (!single){
            JOptionPane.showMessageDialog(null, "Некоторые участники задействованы несколько раз");
            return;
        }

        // 3. Check if date doesn`t intersect with other
        // performances
        Timestamp start, end;
        ArrayList<Performance> perfs = PerformanceController.getAllPerformances();
        try {
            start = java.sql.Timestamp.valueOf(formattedTextField1.getText() + ":00");
            end = java.sql.Timestamp.valueOf(formattedTextField2.getText() + ":00");

        }
        catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "Неверный формат времени");
            return;
        }

        Performance p = new Performance(0, null, null, null, start, end);
        boolean inter = false;
        for (Performance perf : perfs)
            inter |= PerformanceController.doIntersect(p, perf);

        if (inter){
            JOptionPane.showMessageDialog(null, "Спектакль пересекатется по времени с другим");
            return;
        }

        // 4. Add
        int dirId = ((Director)dirsComboBox.getSelectedItem()).getId();
        int musId = ((Musician)musComboBox.getSelectedItem()).getId();
        PerformanceController.addPerformance(playId, start, end, dirId, musId, roles, actors, stunts);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
