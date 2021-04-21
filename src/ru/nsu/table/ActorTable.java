package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.controller.VoiceController;
import ru.nsu.entity.Actor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ActorTable extends AbstractTableModel {
    private ArrayList<Actor> actors;

    private String[] colNames = {
            "Фамилия",
            "Имя",
            "Рост",
            "Голос"
    };

    private final static int LAST_NAME_COL = 0;
    private final static int FIRST_NAME_COL = 1;
    private final static int HEIGHT_COL = 2;
    private final static int VOICE_COL = 3;

    public ActorTable(ArrayList<Actor> actors){
        this.actors = actors;
    }

    @Override
    public int getRowCount() {
        return actors.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Actor act = actors.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL: return act.getLastName();
            case FIRST_NAME_COL: return act.getFirstName();
            case HEIGHT_COL: return act.getHeight();
            case VOICE_COL: return VoiceController.getVoiceNameFromId(act.getVoiceId());
        }

        return null;
    }

    public Actor getActorObj(int row){
        if (row == -1)
            return null;
        return this.actors.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
