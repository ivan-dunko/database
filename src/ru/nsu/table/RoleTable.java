package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.entity.Play;
import ru.nsu.entity.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class RoleTable extends AbstractTableModel {
    private ArrayList<Role> roles;

    private String[] colNames = {
            "Имя",
            "Пол",
            "Минимальный возраст",
            "Максмальный возраст"
    };

    private final static int NAME_COL = 0;
    private final static int SEX_COL = 1;
    private final static int MIN_AGE = 2;
    private final static int MAX_AGE = 3;

    public RoleTable(ArrayList<Role> roles){
        this.roles = roles;
    }

    @Override
    public int getRowCount() {
        return roles.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Role role = roles.get(rowIndex);

        switch (columnIndex){
            case NAME_COL: return role.getName();
            case SEX_COL: return SexController.getSexNameFromId(role.getSexId());
            case MIN_AGE: return role.getMinAge();
            case MAX_AGE: return role.getMaxAge();
        }

        return null;
    }

    public Role getRoleObject(int row){
        if (row == -1)
            return null;
        return this.roles.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    };
}
