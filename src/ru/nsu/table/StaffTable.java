package ru.nsu.table;

import ru.nsu.entity.Staff;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StaffTable extends AbstractTableModel {
    private ArrayList<Staff> staff;

    private String[] colNames = {
            "Фамилия",
            "Имя",
    };

    private final static int LAST_NAME_COL = 0;
    private final static int FIRST_NAME_COL = 1;

    public StaffTable(ArrayList<Staff> staff){
        this.staff = staff;
    }

    @Override
    public int getRowCount() {
        return staff.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Staff mus = staff.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL: return mus.getLastName();
            case FIRST_NAME_COL: return mus.getFirstName();
        }

        return null;
    }

    public Staff getMusicianObj(int row){
        if (row == -1)
            return null;
        return this.staff.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
