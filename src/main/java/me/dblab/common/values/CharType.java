package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;

public class CharType extends Type {
    private static final long serialVersionUID = -4472449011764181054L;

    @Override
    public boolean supports(byte[] value) {
        return value == null || value.length == Character.BYTES;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        return String.valueOf(ByteBuffer.wrap(value).asCharBuffer().get());
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        if (s.length() != 1) {
            throw new StringNotSupportedForTypeException();
        }

        return ByteBuffer.allocate(Character.BYTES).putChar(s.charAt(0)).array();
    }
}
