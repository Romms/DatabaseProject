package me.dblab.common;

import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.RowNotMergeableException;
import me.dblab.exceptions.SchemeNotIntersectCompatibleException;
import me.dblab.exceptions.SchemeNotMergeCompatibleException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Table implements Serializable {
    private static final long serialVersionUID = 5919484961838807424L;

    private Scheme scheme;
    private Map<String, Row> rows;

    public Table(Scheme scheme) {
        this.scheme = scheme;
        this.rows = new HashMap<>();
    }

    public String addRow(Row row) {
        assert row.sameScheme(scheme);

        String id = UUID.randomUUID().toString();
        rows.put(id, row.clone());
        return id;
    }

    public String addRow() {
        return addRow(scheme.createRow());
    }

    public String[] getColumnNames() {
        return scheme.getColumnNames();
    }

    public Row getRow(String id) throws RowNotExistsException {
        Row row = rows.get(id);
        if (row == null) {
            throw new RowNotExistsException(id);
        }

        return row;
    }

    public String[] getRowIds() {
        return rows.keySet().toArray(new String[rows.size()]);
    }

    public void removeRows(String[] rowIds) {
        for (String rowId : rowIds) {
            rows.remove(rowId);
        }
    }

    public Table intersect(Table other) throws SchemeNotIntersectCompatibleException {
        if (!scheme.isSame(other.scheme)) {
            throw new SchemeNotIntersectCompatibleException();
        }

        Table result = new Table(scheme);
        for (Row row : rows.values()) {
            if (other.containsRow(row)) {
                result.addRow(row);
            }
        }

        return result;
    }

    public Table product(Table other) throws SchemeNotMergeCompatibleException {
        Scheme newScheme = scheme.merge(other.scheme);

        Table result = new Table(newScheme);
        for (Row row : rows.values()) {
            for (Row otherRow : other.rows.values()) {
                try {
                    result.addRow(row.merge(otherRow));
                } catch (RowNotMergeableException ignored) {
                }
            }
        }

        return result;
    }

    private boolean containsRow(Row targetRow) throws SchemeNotIntersectCompatibleException {
        if (!targetRow.sameScheme(scheme)) {
            throw new SchemeNotIntersectCompatibleException();
        }

        for (Row row : rows.values()) {
            if (targetRow.equals(row)) {
                return true;
            }
        }

        return false;
    }
}
