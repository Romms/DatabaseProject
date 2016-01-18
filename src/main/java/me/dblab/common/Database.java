package me.dblab.common;

import me.dblab.common.values.Type;
import me.dblab.exceptions.SchemeNotIntersectCompatibleException;
import me.dblab.exceptions.SchemeNotMergeCompatibleException;
import me.dblab.exceptions.TableAlreadyExistsException;
import me.dblab.exceptions.TableNotExistsException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Database implements Serializable {
    private static final long serialVersionUID = -8959232483463892607L;

    private Map<String, Table> tables;

    public Database() {
        tables = new HashMap<>();
    }

    public void createTable(String name, Table table) throws TableAlreadyExistsException {
        if (tables.containsKey(name)) {
            throw new TableAlreadyExistsException(name);
        }

        tables.put(name, table);
    }

    public void createTable(String name, Scheme scheme) throws TableAlreadyExistsException {
        createTable(name, new Table(scheme));
    }

    public String[] getTableNames() {
        return tables.keySet().toArray(new String[tables.size()]);
    }

    public void removeTable(String tableName) throws TableNotExistsException {
        if (!tables.containsKey(tableName)) {
            throw new TableNotExistsException(tableName);
        }

        tables.remove(tableName);
    }

    public void addRow(String tableName) throws TableNotExistsException {
        if (!tables.containsKey(tableName)) {
            throw new TableNotExistsException(tableName);
        }

        tables.get(tableName).addRow();
    }

    public Table getTable(String tableName) throws TableNotExistsException {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new TableNotExistsException(tableName);
        }
        return table;
    }

    public void intersectTables(String tableName1, String tableName2, String tableNameResult) throws TableNotExistsException, TableAlreadyExistsException, SchemeNotIntersectCompatibleException {
        Table table1 = getTable(tableName1);
        Table table2 = getTable(tableName2);

        Table tableResult = table1.intersect(table2);
        createTable(tableNameResult, tableResult);
    }

    public void productTables(String tableName1, String tableName2, String tableNameResult) throws TableNotExistsException, SchemeNotIntersectCompatibleException, TableAlreadyExistsException, SchemeNotMergeCompatibleException {
        Table table1 = getTable(tableName1);
        Table table2 = getTable(tableName2);

        Table tableResult = table1.product(table2);
        createTable(tableNameResult, tableResult);
    }

    public String[] getSupportedTypes(){
        Set<String> types = Type.ExistingTypes().keySet();
        return Arrays.copyOf(types.toArray(), types.size(), String[].class);
    }
}
