package me.dblab.common.values;

import me.dblab.exceptions.StringNotSupportedForTypeException;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DateIntervalType extends Type  {

    private static final long serialVersionUID = -2580310918795798489L;
    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public boolean supports(byte[] value) {
        return value == null || value.length == Long.BYTES*2;
    }

    @Override
    public String toString(byte[] value) {
        assert supports(value);

        if (value == null) {
            return null;
        }

        byte[] value_first = Arrays.copyOfRange(value, 0, value.length/2);
        byte[] value_second = Arrays.copyOfRange(value, value.length/2, value.length);

        Date first = new Date( ByteBuffer.wrap(value_first ).asLongBuffer().get() );
        Date second = new Date( ByteBuffer.wrap(value_second).asLongBuffer().get() );
        return dateFormatter.format(first) + " - "  + dateFormatter.format(second);
    }

    @Override
    public byte[] fromString(String s) throws StringNotSupportedForTypeException {
        if (s == null) {
            return null;
        }

        System.out.println("("+s+")");
        String[] dates = s.split(" - ");

        Date first;
        Date second;
        try {
            first = dateFormatter.parse(dates[0].trim());
            second = dateFormatter.parse(dates[1].trim());
        } catch (ParseException e) {
            throw new StringNotSupportedForTypeException();
        }

        return ByteBuffer.allocate(Long.BYTES*2).putLong(first.getTime()).putLong(Long.BYTES, second.getTime()).array();
    }
}
