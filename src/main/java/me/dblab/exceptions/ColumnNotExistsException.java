package me.dblab.exceptions;

public class ColumnNotExistsException extends Exception {
    private final String columnName;

    public ColumnNotExistsException(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getMessage() {
        return "Column " + columnName + " doesn't exists in the table";
    }
}
