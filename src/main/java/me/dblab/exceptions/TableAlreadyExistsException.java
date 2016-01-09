package me.dblab.exceptions;

public class TableAlreadyExistsException extends Exception {
    private final String tableName;

    public TableAlreadyExistsException(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getMessage() {
        return "Table " + tableName + " already exists";
    }
}
