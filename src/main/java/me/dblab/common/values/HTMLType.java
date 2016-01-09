package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

public class HTMLType extends Type {
    @Override
    public boolean supports(byte[] value) {
        return new StringType().supports(value);
    }

    @Override
    public String toString(byte[] value) {
        return new StringType().toString(value);
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        return new StringType().fromString(s);
    }
}
