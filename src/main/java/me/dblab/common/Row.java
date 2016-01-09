package me.dblab.common;

import me.dblab.common.values.Value;
import me.dblab.exceptions.RowNotMergeableException;
import me.dblab.exceptions.StringNotSupportedForTypeException;
import me.dblab.common.values.Type;
import me.dblab.exceptions.WrongBytesArrayException;
import me.dblab.exceptions.ColumnNotExistsException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Row implements Serializable, Cloneable {
    private static final long serialVersionUID = -1022594796689189276L;

    Map<String, Value> values;

    public Row(Map<String, Type> columns) {
        values = new HashMap<>();
        for (Map.Entry<String, Type> column: columns.entrySet()) {
            try {
                values.put(column.getKey(), column.getValue().createWithValue(null));
            } catch (WrongBytesArrayException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Type> getColumns() {
        Map<String, Type> columns = new HashMap<>();
        for (Map.Entry<String, Value> entry: values.entrySet()) {
            columns.put(entry.getKey(), entry.getValue().getType());
        }
        return columns;
    }

    public Value getValue(String columnName) throws ColumnNotExistsException {
        Value value = values.get(columnName);
        if (value == null) {
            throw new ColumnNotExistsException(columnName);
        }

        return value;
    }

    public void setValue(String columnName, String value) throws ColumnNotExistsException, StringNotSupportedForTypeException {
        if (!values.containsKey(columnName)) {
            throw new ColumnNotExistsException(columnName);
        }

        values.get(columnName).setValue(value);
    }

    public boolean sameScheme(Scheme scheme) {
        // TODO: Fix the bug where scheme being compared to the same scheme.
        return scheme.isSame(scheme);
    }

    @Override
    public Row clone() {
        Row row = new Row(getColumns());
        for (Map.Entry<String, Value> entry : values.entrySet()) {
            try {
                row.values.put(entry.getKey(), entry.getValue().getType().createWithValue(entry.getValue().getValue()));
            } catch (WrongBytesArrayException e) {
                e.printStackTrace();
            }
        }
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        if (values.size() != row.values.size()) {
            return false;
        }

        for (Map.Entry<String, Value> entry : values.entrySet()) {
            Value otherValue = row.values.get(entry.getKey());
            if (!entry.getValue().equals(otherValue)) {
                return false;
            }
        }

        return true;
    }

    public boolean commonColumnsSameValue(Row other) {
        for (Map.Entry<String, Value> entry : values.entrySet()) {
            Value value = entry.getValue();
            Value otherValue = other.values.get(entry.getKey());
            if (other.values.containsKey(entry.getKey()) && !value.equals(otherValue)) {
                return false;
            }
        }

        return true;
    }

    public Row merge(Row other) throws RowNotMergeableException {
        if (!commonColumnsSameValue(other)) {
            throw new RowNotMergeableException();
        }

        Map<String, Type> columns = getColumns();
        columns.putAll(other.getColumns());

        Row newRow = new Row(columns);
        for (Map.Entry<String, Type> entry : columns.entrySet()) {
            String columnName = entry.getKey();
            Value value = values.get(columnName);
            if (value == null) {
                value = other.values.get(columnName);
            }
            newRow.values.put(columnName, value);
        }

        return newRow;
    }
}
