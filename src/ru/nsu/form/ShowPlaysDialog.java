package ru.nsu.form;

import ru.nsu.entity.Author;
import ru.nsu.entity.Play;
import ru.nsu.table.AuthorTable;
import ru.nsu.table.PlayTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ShowPlaysDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable showPlaysTable;

    public ShowPlaysDialog(Author author, Connection conn) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // load plays into table
        new SwingWorker<ArrayList<Play>, Void>(){
            Author auth = author;
            JTable playsTable = showPlaysTable;
            Connection connection = conn;
            @Override
            protected ArrayList<Play> doInBackground() throws Exception {
                PreparedStatement stmt = connection.prepareStatement("select * from plays " +
                        "join authors on authors.id = plays.author_id " +
                        "where authors.id = ?");
                stmt.setInt(1, auth.getId());
                stmt.execute();

                ResultSet resultSet = stmt.getResultSet();
                ArrayList<Play> res = new ArrayList<>();
                while (resultSet.next())
                    res.add(new Play(resultSet));

                return res;
            }

            @Override
            protected void done() {
                try {
                    playsTable.setModel(new PlayTable(get()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

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
