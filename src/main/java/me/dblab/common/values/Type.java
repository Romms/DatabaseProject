package me.dblab.common.values;

import me.dblab.exceptions.InvalidTypeException;
import me.dblab.exceptions.StringNotSupportedForTypeException;
import me.dblab.exceptions.WrongBytesArrayException;

import java.io.Serializable;

public abstract class Type implements Serializable {
    private static final String INT_TYPE = "Int";
    private static final String DOUBLE_TYPE = "Double";
    private static final String CHAR_TYPE = "Char";
    private static final String LONG_TYPE = "Long";
    private static final String STRING_TYPE = "String";
    private static final String HTML_TYPE = "HTML";

    public abstract boolean supports(byte[] value);

    public abstract String toString(byte[] value);

    public Value createWithValue(byte[] value) throws WrongBytesArrayException {
        return new Value(this, value);
    }

    public static Type typeFromString(String type) throws InvalidTypeException {
        switch (type) {
            case INT_TYPE:
                return new IntType();
            case DOUBLE_TYPE:
                return new DoubleType();
            case CHAR_TYPE:
                return new CharType();
            case LONG_TYPE:
                return new LongType();
            case STRING_TYPE:
                return new StringType();
            case HTML_TYPE:
                return new HTMLType();
            default:
                throw new InvalidTypeException();
        }
    }

    public abstract byte[] fromString(String s) throws StringNotSupportedForTypeException;

    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }
}
