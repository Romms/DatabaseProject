package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;

public class DoubleType extends Type {
    private static final long serialVersionUID = -2960162520866747532L;

    @Override
    public boolean supports(byte[] value) {
        return value == null || value.length == Double.BYTES;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        return String.valueOf(ByteBuffer.wrap(value).asDoubleBuffer().get());
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        Double value;
        try {
            value = Double.parseDouble(s);
        } catch(NumberFormatException e) {
            throw new StringNotSupportedForTypeException();
        }


        return ByteBuffer.allocate(Double.BYTES).putDouble(value).array();
    }
}
