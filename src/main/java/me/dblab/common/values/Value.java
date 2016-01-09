package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;
import me.dblab.exceptions.WrongBytesArrayException;

import java.io.Serializable;
import java.util.Arrays;

public class Value implements Serializable {
    private static final long serialVersionUID = 2069760024414323872L;
    Type type;
    byte[] value;

    public Value(Type type, byte[] value) throws WrongBytesArrayException {
        this.type = type;
        this.value = value;
        if (!type.supports(value)) {
            throw new WrongBytesArrayException();
        }
    }

    public Value(Type type) {
        this.type = type;
        this.value = null;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) throws WrongBytesArrayException {
        if (!type.supports(value)) {
            throw new WrongBytesArrayException();
        }
        this.value = value;
    }

    public void setValue(String value) throws StringNotSupportedForTypeException {
        try {
            setValue(type.fromString(value));
        } catch (WrongBytesArrayException e) {
            assert false;
        }
    }

    public String toString() {
        return type.toString(value);
    }

    public boolean isNull() {
        return value == null;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value1 = (Value) o;

        return type.equals(value1.type) && Arrays.equals(value, value1.value);
    }
}
