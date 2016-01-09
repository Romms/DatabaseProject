package me.dblab.client.clientcontroller;

import me.dblab.client.misc.DatabaseUpdateListener;
import me.dblab.client.misc.MessageListener;
import me.dblab.exceptions.*;
import me.dblab.databasecontroller.DatabaseController;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class ClientController {
    private List<DatabaseUpdateListener> updateListeners;
    private List<MessageListener> messageListeners;
    private DatabaseController databaseController;
    private String activeTable;

    public ClientController(DatabaseController controller) {
        updateListeners = new LinkedList<>();
        messageListeners = new LinkedList<>();
        this.databaseController = controller;
        activeTable = null;
    }

    public void newDatabase() {
        try {
            databaseController.createNewDatabase();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void loadDatabase() {
        String path = ClientControllerHelper.queryLoadPath();
        if (path == null) {
            return;
        }
        try {
            databaseController.loadDatabaseFromFile(path);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void saveDatabase() {
        String path = ClientControllerHelper.querySavePath();
        if (path == null) {
            return;
        }
        try {
            databaseController.saveDatabaseToFile(path);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            databaseController.closeDatabase();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void createTable() {
        String name = ClientControllerHelper.queryTableName();
        if (name == null) {
            return;
        }

        String scheme = ClientControllerHelper.queryTableScheme();
        if (scheme == null) {
            return;
        }

        try {
            databaseController.createNewTable(name, scheme);
        } catch (InvalidSchemeDescriptionException | RemoteException e) {
            e.printStackTrace();
        } catch (TableAlreadyExistsException e) {
            notifyMessageListeners(e.getMessage());
        }

        notifyUpdateListeners();
    }

    public void removeTable() {
        String tableName = ClientControllerHelper.queryTableName();
        if (tableName == null) {
            return;
        }

        try {
            databaseController.removeTable(tableName);
        } catch (TableNotExistsException | RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void removeTable(String tableName) {
        try {
            databaseController.removeTable(tableName);
        } catch (TableNotExistsException | RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void intersectTables() {
        String tableName1 = ClientControllerHelper.queryTableName();
        if (tableName1 == null) {
            return;
        }

        String tableName2 = ClientControllerHelper.queryTableName();
        if (tableName2 == null) {
            return;
        }

        String tableNameResult = ClientControllerHelper.queryTableName();
        if (tableNameResult == null) {
            return;
        }

        try {
            databaseController.intersectTables(tableName1, tableName2, tableNameResult);
        } catch (SchemeNotIntersectCompatibleException | TableAlreadyExistsException | TableNotExistsException | RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void productTables() {
        String tableName1 = ClientControllerHelper.queryTableName();
        if (tableName1 == null) {
            return;
        }

        String tableName2 = ClientControllerHelper.queryTableName();
        if (tableName2 == null) {
            return;
        }

        String tableNameResult = ClientControllerHelper.queryTableName();
        if (tableNameResult == null) {
            return;
        }

        try {
            databaseController.productTables(tableName1, tableName2, tableNameResult);
        } catch (RemoteException | SchemeNotMergeCompatibleException | TableNotExistsException | TableAlreadyExistsException | SchemeNotIntersectCompatibleException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void addUpdateListener(DatabaseUpdateListener listener) {
        updateListeners.add(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public DatabaseController getDatabaseController() {
        return databaseController;
    }

    public void notifyUpdateListeners() {
        updateListeners.forEach(DatabaseUpdateListener::handleDatabaseUpdate);
    }

    public void notifyMessageListeners(String message) {
        for (MessageListener listener : messageListeners) {
            listener.handleMessage(message);
        }
    }

    public String getActiveTable() {
        return activeTable;
    }

    public void addEmptyRow(String tableName) {
        activeTable = tableName;
        try {
            databaseController.addEmptyRow(tableName);
        } catch (TableNotExistsException | RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void databaseTableSetValue(String tableName, String rowId, String columnName, String value) {
        activeTable = tableName;
        try {
            databaseController.tableSetValue(tableName, rowId, columnName, value);
        } catch (TableNotExistsException | RowNotExistsException | ColumnNotExistsException | RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }

    public void removeTableRows(String tableName, String[] rowIdsToDelete) throws TableNotExistsException {
        activeTable = tableName;
        try {
            databaseController.removeTableRows(tableName, rowIdsToDelete);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        notifyUpdateListeners();
    }
}
