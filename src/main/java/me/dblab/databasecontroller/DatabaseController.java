package me.dblab.databasecontroller;

import me.dblab.exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.Remote;
import java.rmi.RemoteException;

@WebService // Endpoint Interface
@SOAPBinding(style = SOAPBinding.Style.RPC) // Needed for the WSDL
public interface DatabaseController extends Remote {
    @WebMethod boolean isOpened() throws RemoteException;
    @WebMethod void createNewDatabase() throws RemoteException;
    @WebMethod void loadDatabaseFromFile(String path) throws RemoteException;
    @WebMethod void saveDatabaseToFile(String path) throws RemoteException;
    @WebMethod void closeDatabase() throws RemoteException;
    @WebMethod String[] getTableNames() throws RemoteException;
    @WebMethod String[] getSupportedTypes() throws RemoteException;
    @WebMethod void createNewTable(String name, String scheme) throws InvalidSchemeDescriptionException, TableAlreadyExistsException, RemoteException;
    @WebMethod void removeTable(String tableName) throws TableNotExistsException, RemoteException;
    @WebMethod void intersectTables(String tableName1, String tableName2, String tableNameResult) throws SchemeNotIntersectCompatibleException, TableAlreadyExistsException, TableNotExistsException, RemoteException;
    @WebMethod void productTables(String tableName1, String tableName2, String tableNameResult) throws RemoteException, TableNotExistsException, SchemeNotIntersectCompatibleException, TableAlreadyExistsException, SchemeNotMergeCompatibleException;
    @WebMethod void addEmptyRow(String tableName) throws TableNotExistsException, RemoteException;
    @WebMethod String[] getTableRowIds(String tableName) throws TableNotExistsException, RemoteException;
    @WebMethod String[] getTableColumns(String tableName) throws TableNotExistsException, RemoteException;
    @WebMethod String tableGetValue(String tableName, String rowId, String columnName) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException, RemoteException;
    @WebMethod void tableSetValue(String tableName, String rowId, String columnName, String value) throws TableNotExistsException, RowNotExistsException, ColumnNotExistsException, RemoteException;
    @WebMethod void removeTableRows(String tableName, String[] rowIdsToDelete) throws TableNotExistsException, RemoteException;
}
