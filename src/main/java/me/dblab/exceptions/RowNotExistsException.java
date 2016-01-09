package me.dblab.exceptions;

public class RowNotExistsException extends Exception {
    private final String id;

    public RowNotExistsException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Row with id " + id + " doesn't exists in the table";
    }
}
