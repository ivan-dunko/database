package ru.nsu.table;

import ru.nsu.entity.Author;
import ru.nsu.entity.Performance;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PerformanceTable extends AbstractTableModel {
    private ArrayList<Performance> perfs;

    private String[] colNames = {
            "Режиссёр",
            "Музыкант",
            "Пьеса",
            "Начало",
            "Конец"
    };

    private final static int DIR_COL = 0;
    private final static int MUS_COL = 1;
    private final static int PLAY_COL = 2;
    private final static int START_TIME_COL = 3;
    private final static int END_TIME_COL = 4;

    public PerformanceTable(ArrayList<Performance> perfs){
        this.perfs = perfs;
    }

    @Override
    public int getRowCount() {
        return perfs.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Performance perf = perfs.get(rowIndex);

        switch (columnIndex){
            case DIR_COL: return perf.getDirector();
            case MUS_COL: return perf.getMusician();
            case PLAY_COL: return perf.getPlay();
            case START_TIME_COL: return perf.getStartTime().toString();
            case END_TIME_COL: return  perf.getEndTime().toString();
        }

        return null;
    }

    public Performance getPerformanceObject(int row){
        if (row == -1)
            return null;
        return this.perfs.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
