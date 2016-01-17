package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;

public class LongType extends Type {
    private static final long serialVersionUID = -2350523799038870787L;

    @Override
    public boolean supports(byte[] value) {
        return value == null || value.length == Long.BYTES;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        return String.valueOf(ByteBuffer.wrap(value).asLongBuffer().get());
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        Long value;
        try {
            value = Long.parseLong(s);
        } catch(NumberFormatException e) {
            throw new StringNotSupportedForTypeException();
        }



        return ByteBuffer.allocate(Long.BYTES).putDouble(value).array();
    }
}
