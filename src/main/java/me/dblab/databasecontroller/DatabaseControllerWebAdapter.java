package me.dblab.databasecontroller;

import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Base64;

@WebService(serviceName = "DatabaseControllerService", endpointInterface = "me.dblab.databasecontroller.DatabaseControllerWeb")
public class DatabaseControllerWebAdapter extends DatabaseControllerNullEscapeAdapter implements DatabaseControllerWeb {
    public DatabaseControllerWebAdapter(DatabaseController controller) {
        super(controller);
    }

    private static final String DELIMITER = ";";

    private String encodeTable(String tableName) throws RemoteException, TableNotExistsException, RowNotExistsException, ColumnNotExistsException {
        return toBase64(toBase64(tableName) +
                        DELIMITER +
                        encodeColumns(tableName) +
                        DELIMITER +
                        encodeRows(tableName));
    }

    private String encodeColumns(String tableName) throws RemoteException, TableNotExistsException {
        String[] columnNames = getTableColumns(tableName);
        String[] encodedColumns = new String[columnNames.length];
        for (int i = 0; i < columnNames.length; ++i) {
            encodedColumns[i] = encodeColumn(columnNames[i]);
        }
        return toBase64(String.join(DELIMITER, encodedColumns));
    }

    private String encodeColumn(String columnName) {
        return toBase64(columnName);
    }

    private String encodeRows(String tableName) throws RemoteException, TableNotExistsException, RowNotExistsException, ColumnNotExistsException {
        String[] rowIds = getTableRowIds(tableName);
        String[] encodedRows = new String[rowIds.length];
        for (int i = 0; i < rowIds.length; ++i) {
            encodedRows[i] = encodeRow(tableName, rowIds[i]);
        }
        return toBase64(String.join(DELIMITER, encodedRows));
    }

    private String encodeRow(String tableName, String rowId) throws RemoteException, TableNotExistsException, RowNotExistsException, ColumnNotExistsException {
        String[] columnNames = getTableColumns(tableName);
        String[] encodedRow = new String[1 + columnNames.length];
        encodedRow[0] = toBase64(rowId);
        for (int i = 0; i < columnNames.length; ++i) {
            encodedRow[i + 1] = toBase64(tableGetValue(tableName, rowId, columnNames[i]));
        }
        return toBase64(String.join(DELIMITER, encodedRow));
    }

    @Override
    public String getDatabase() throws RemoteException {
        try {
            String[] tableNames = getTableNames();
            String[] encodedTables = new String[tableNames.length];
            for (int i = 0; i < tableNames.length; ++i) {
                encodedTables[i] = encodeTable(tableNames[i]);
            }
            return String.join(DELIMITER, encodedTables);
        } catch (RowNotExistsException | ColumnNotExistsException | TableNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toBase64(String s) {
        try {
            return Base64.getEncoder().encodeToString(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}
