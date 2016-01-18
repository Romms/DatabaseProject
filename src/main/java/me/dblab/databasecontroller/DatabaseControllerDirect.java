package me.dblab.databasecontroller;

import me.dblab.common.*;
import me.dblab.exceptions.*;
import me.dblab.common.Scheme;
import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

import java.io.*;

public class DatabaseControllerDirect implements DatabaseController {
    Database database;

    public DatabaseControllerDirect() {
        database = null;
    }

    @Override
    public boolean isOpened() {
        return database != null;
    }

    @Override
    public void createNewDatabase() {
        database = new Database();
    }

    @Override
    public void loadDatabaseFromFile(String path) {
        database = null;
        try {
            Object db = new ObjectInputStream(new FileInputStream(path)).readObject();
            if (db instanceof Database) {
                database = (Database) db;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDatabaseToFile(String path) {
        try {
            new ObjectOutputStream(new FileOutputStream(path)).writeObject(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeDatabase() {
        database = null;
    }

    @Override
    public String[] getTableNames() {
        return database.getTableNames();
    }

    @Override
    public String[] getSupportedTypes() {
        if(null == database) {
            return new String[0];
        }
        else
            return database.getSupportedTypes();
    }

    @Override
    public void createNewTable(String name, String scheme) throws InvalidSchemeDescriptionException, TableAlreadyExistsException {
        database.createTable(name, Scheme.createFromString(scheme));
    }

    @Override
    public void removeTable(String tableName) throws TableNotExistsException {
        database.removeTable(tableName);
    }

    @Override
    public void intersectTables(String tableName1, String tableName2, String tableNameResult) throws SchemeNotIntersectCompatibleException, TableAlreadyExistsException, TableNotExistsException {
        database.intersectTables(tableName1, tableName2, tableNameResult);
    }

    @Override
    public void productTables(String tableName1, String tableName2, String tableNameResult) throws TableNotExistsException, SchemeNotIntersectCompatibleException, TableAlreadyExistsException, SchemeNotMergeCompatibleException {
        database.productTables(tableName1, tableName2, tableNameResult);
    }

    @Override
    public void addEmptyRow(String tableName) throws TableNotExistsException {
        database.addRow(tableName);
    }

    @Override
    public String[] getTableRowIds(String tableName) throws TableNotExistsException {
        Table table = database.getTable(tableName);
        if (table == null) {
            throw new TableNotExistsException(tableName);
        }

        return table.getRowIds();
    }

    @Override
    public String[] getTableColumns(String tableName) throws TableNotExistsException {
        return database.getTable(tableName).getColumnNames();
    }

    @Override
    public String tableGetValue(String tableName, String rowId, String columnName) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException {
        return database.getTable(tableName).getRow(rowId).getValue(columnName).toString();
    }

    @Override
    public void tableSetValue(String tableName, String rowId, String columnName, String value) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException {
        try {
            database.getTable(tableName).getRow(rowId).setValue(columnName, value);
        } catch (StringNotSupportedForTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTableRows(String tableName, String[] rowIdsToDelete) throws TableNotExistsException {
        database.getTable(tableName).removeRows(rowIdsToDelete);
    }
}
