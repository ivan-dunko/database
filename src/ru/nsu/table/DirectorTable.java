package ru.nsu.table;

import ru.nsu.entity.Director;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DirectorTable extends AbstractTableModel {
    private ArrayList<Director> directors;

    private String[] colNames = {
            "Фамилия",
            "Имя",
    };

    private final static int LAST_NAME_COL = 0;
    private final static int FIRST_NAME_COL = 1;

    public DirectorTable(ArrayList<Director> directors){
        this.directors = directors;
    }

    @Override
    public int getRowCount() {
        return directors.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Director dir = directors.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL: return dir.getLastName();
            case FIRST_NAME_COL: return dir.getFirstName();
        }

        return null;
    }

    public Director getActorObj(int row){
        if (row == -1)
            return null;
        return this.directors.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
