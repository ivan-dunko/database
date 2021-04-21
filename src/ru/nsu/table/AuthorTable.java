package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.entity.Author;
import ru.nsu.entity.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class AuthorTable extends AbstractTableModel {
    private ArrayList<Author> authors;

    private String[] colNames = {
            "Фамилия",
            "Имя",
            "Дата рождения",
            "Дата смерти"
    };

    private final static int LAST_NAME_COL = 0;
    private final static int FIRST_NAME_COL = 1;
    private final static int BIRTH_DATE_COL = 2;
    private final static int DEATH_DATE_COL = 3;

    public AuthorTable(ArrayList<Author> authors){
        this.authors = authors;
    }

    @Override
    public int getRowCount() {
        return authors.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Author author = authors.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL: return author.getLastName();
            case FIRST_NAME_COL: return author.getFirstName();
            case BIRTH_DATE_COL: return author.getBirthDate().toString();
            case DEATH_DATE_COL: return author.getDeathDate().toString();
        }

        return null;
    }

    public Author getAuthorObj(int row){
        if (row == -1)
            return null;
        return this.authors.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
