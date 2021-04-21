package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.entity.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PlayTable extends AbstractTableModel {
    private ArrayList<Employee> employees;

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

    public EmployeeTable(ArrayList<Employee> employees){
        this.employees = employees;
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee emp = employees.get(rowIndex);

        switch (columnIndex){
            case LAST_NAME_COL: return emp.getLastName();
            case FIRST_NAME_COL: return emp.getFirstName();
            case BIRTH_DATE_COL: return emp.getBirthDate().toString();
            case SALARY_COL: return emp.getSalary();
            case EXP_COL: return emp.getExperience();
            case NATIVE_COL: return emp.isNative();
            case SEX_COL: return SexController.getSexNameFromId(emp.getSexId());
        }

        return null;
    }

    public Employee getEmployeeObj(int row){
        if (row == -1)
            return null;
        return this.employees.get(row);
    }

    @Override
    public String getColumnName(int column){
        return colNames[column];
    }
}
