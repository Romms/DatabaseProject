package me.dblab.databasecontroller;

import me.dblab.exceptions.TableNotExistsException;

import java.rmi.RemoteException;

public class DatabaseControllerWebNullUnescapeAdapter extends DatabaseControllerNullUnescapeAdapter implements DatabaseControllerWeb {
    private DatabaseControllerWeb controller;

    public DatabaseControllerWebNullUnescapeAdapter(DatabaseControllerWeb controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public String getDatabase() throws RemoteException, TableNotExistsException {
        return controller.getDatabase();
    }
}
