package me.dblab.common;

import me.dblab.exceptions.InvalidTypeException;
import me.dblab.common.values.Type;
import me.dblab.exceptions.InvalidSchemeDescriptionException;
import me.dblab.exceptions.SchemeNotMergeCompatibleException;

import java.io.Serializable;
import java.util.*;

public class Scheme implements Serializable {
    private static final long serialVersionUID = -68984829957076057L;

    private Map<String, Type> columns;

    public Scheme(Iterable<Column> columns) {
        this.columns = new HashMap<>();
        for (Column column : columns) {
            this.columns.put(column.getName(), column.getType());
        }
    }

    public static Scheme createFromString(String s) throws InvalidSchemeDescriptionException {
        List<Column> columns = new LinkedList<>();

        for (String columnDesctiption : s.split(";")) {
            String[] tokens = columnDesctiption.split(",");
            if (tokens.length != 2) {
                throw new InvalidSchemeDescriptionException();
            }

            String name = tokens[0];
            String type = tokens[1];
            try {
                columns.add(new Column(name, Type.typeFromString(type)));
            } catch (InvalidTypeException e) {
                throw new InvalidSchemeDescriptionException();
            }
        }

        return new Scheme(columns);
    }

    public Row createRow() {
        return new Row(columns);
    }

    public String[] getColumnNames() {
        return columns.keySet().toArray(new String[columns.size()]);
    }

    public boolean isSame(Scheme other) {
        return columns.size() == other.columns.size() && sameNameSameType(other);
    }

    public boolean sameNameSameType(Scheme other) {
        for (Map.Entry<String, Type> entry : columns.entrySet()) {
            Type type = other.columns.get(entry.getKey());
            if (!entry.getValue().equals(type)) {
                return false;
            }
        }

        return true;
    }

    public Scheme merge(Scheme other) throws SchemeNotMergeCompatibleException {
        Set<Column> newColumns = new HashSet<>();
        for (Map.Entry<String, Type> entry : columns.entrySet()) {
            newColumns.add(new Column(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Type> entry : other.columns.entrySet()) {
            newColumns.add(new Column(entry.getKey(), entry.getValue()));
        }
        return new Scheme(newColumns);
    }
}
