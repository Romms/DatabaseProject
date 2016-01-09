package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;

public class IntType extends Type {
    private static final long serialVersionUID = 3377301209632200331L;

    @Override
    public boolean supports(byte[] value) {
        return value == null || value.length == Integer.BYTES;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        return String.valueOf(ByteBuffer.wrap(value).asIntBuffer().get());
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        return ByteBuffer.allocate(Integer.BYTES).putInt(Integer.parseInt(s)).array();
    }
}
