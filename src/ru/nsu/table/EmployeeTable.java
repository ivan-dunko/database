package ru.nsu.table;

import ru.nsu.controller.SexController;
import ru.nsu.entity.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class EmployeeTable extends AbstractTableModel {
    private ArrayList<Employee> employees;

    private String[] colNames = {
            "Фамилия",
            "Имя",
            "Дата рождения",
            "З/п",
            "Опыт",
            "Здешний",
            "Пол"
    };

    private final static int LAST_NAME_COL = 0;
    private final static int FIRST_NAME_COL = 1;
    private final static int BIRTH_DATE_COL = 2;
    private final static int SALARY_COL = 3;
    private final static int EXP_COL = 4;
    private final static int NATIVE_COL = 5;
    private final static int SEX_COL = 6;

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
