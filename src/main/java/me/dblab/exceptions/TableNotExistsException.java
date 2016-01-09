package me.dblab.exceptions;

public class TableNotExistsException extends Exception {
    private final String tableName;

    public TableNotExistsException(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "Table " + tableName + " doesn't exists";
    }
}
