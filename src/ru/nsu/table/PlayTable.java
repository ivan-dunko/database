package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.entity.Employee;
import ru.nsu.entity.Play;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PlayTable extends AbstractTableModel {
    private ArrayList<Play> plays;

    private String[] colNames = {
            "Название",
            "Автор",
            "Год",
            "Жанр",
            "Возрастная категория",
    };

    private final static int NAME_COL = 0;
    private final static int AUTHOR_COL = 1;
    private final static int YEAR_COL = 2;
    private final static int GENRE_COL = 3;
    private final static int CATEGORY_COL = 4;

    public PlayTable(ArrayList<Play> plays){
        this.plays = plays;
    }

    @Override
    public int getRowCount() {
        return plays.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Play play = plays.get(rowIndex);

        switch (columnIndex){
            case NAME_COL: return play.getName();
            case AUTHOR_COL: return play.getAuthor();
            case YEAR_COL: return play.getYear();
            case GENRE_COL: return play.getGenreId();
            case CATEGORY_COL: return play.getAgeCatId();
        }

        return null;
    }

    public Play getEmployeeObj(int row){
        if (row == -1)
            return null;
        return this.plays.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
