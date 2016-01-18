package me.dblab.databasecontroller;

import me.dblab.exceptions.*;

import java.rmi.RemoteException;

public class DatabaseControllerNullUnescapeAdapter implements DatabaseController {
    private DatabaseController controller;

    public DatabaseControllerNullUnescapeAdapter(DatabaseController controller) {
        this.controller = controller;
    }

    @Override
    public boolean isOpened() throws RemoteException {
        return controller.isOpened();
    }

    @Override
    public void createNewDatabase() throws RemoteException {
        controller.createNewDatabase();
    }

    @Override
    public void loadDatabaseFromFile(String path) throws RemoteException {
        controller.loadDatabaseFromFile(path);
    }

    @Override
    public void saveDatabaseToFile(String path) throws RemoteException {
        controller.saveDatabaseToFile(path);
    }

    @Override
    public void closeDatabase() throws RemoteException {
        controller.closeDatabase();
    }

    @Override
    public String[] getTableNames() throws RemoteException {
        return controller.getTableNames();
    }

    @Override
    public String[] getSupportedTypes() throws RemoteException {
        return controller.getSupportedTypes();
    }

    @Override
    public void createNewTable(String name, String scheme) throws InvalidSchemeDescriptionException, TableAlreadyExistsException, RemoteException {
        controller.createNewTable(name, scheme);
    }

    @Override
    public void removeTable(String tableName) throws TableNotExistsException, RemoteException {
        controller.removeTable(tableName);
    }

    @Override
    public void intersectTables(String tableName1, String tableName2, String tableNameResult) throws SchemeNotIntersectCompatibleException, TableAlreadyExistsException, TableNotExistsException, RemoteException {
        controller.intersectTables(tableName1, tableName2, tableNameResult);
    }

    @Override
    public void productTables(String tableName1, String tableName2, String tableNameResult) throws RemoteException, TableNotExistsException, SchemeNotIntersectCompatibleException, SchemeNotMergeCompatibleException, TableAlreadyExistsException {
        controller.productTables(tableName1, tableName2, tableNameResult);
    }

    @Override
    public void addEmptyRow(String tableName) throws TableNotExistsException, RemoteException {
        controller.addEmptyRow(tableName);
    }

    @Override
    public String[] getTableRowIds(String tableName) throws TableNotExistsException, RemoteException {
        return controller.getTableRowIds(tableName);
    }

    @Override
    public String[] getTableColumns(String tableName) throws TableNotExistsException, RemoteException {
        return controller.getTableColumns(tableName);
    }

    @Override
    public String tableGetValue(String tableName, String rowId, String columnName) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException, RemoteException {
        return NullEscaper.nullUnescape(controller.tableGetValue(tableName, rowId, columnName));
    }

    @Override
    public void tableSetValue(String tableName, String rowId, String columnName, String value) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException, RemoteException {
        controller.tableSetValue(tableName, rowId, columnName, NullEscaper.nullEscape(value));
    }

    @Override
    public void removeTableRows(String tableName, String[] rowIdsToDelete) throws TableNotExistsException, RemoteException {
        controller.removeTableRows(tableName, rowIdsToDelete);
    }
}
