package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.io.UnsupportedEncodingException;

public class StringType extends Type {
    private static final long serialVersionUID = 7494966750200367539L;

    @Override
    public boolean supports(byte[] value) {
        return true;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        String result = null;
        try {
            result = new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        byte[] result = null;
        try {
            result = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
