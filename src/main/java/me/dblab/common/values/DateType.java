package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateType extends Type  {

    private static final long serialVersionUID = -9083477378398844042L;
    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


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


        Date d = new Date( ByteBuffer.wrap(value).asLongBuffer().get() );
        return dateFormatter.format(d);
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        Date value;
        try {
            value = dateFormatter.parse(s);
        } catch (ParseException e) {
            throw new StringNotSupportedForTypeException();
        }

        return ByteBuffer.allocate(Long.BYTES).putLong(value.getTime()).array();
    }
}
