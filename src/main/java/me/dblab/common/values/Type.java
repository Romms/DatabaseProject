package me.dblab.common.values;

import me.dblab.exceptions.InvalidTypeException;
import me.dblab.exceptions.StringNotSupportedForTypeException;
import me.dblab.exceptions.WrongBytesArrayException;
//import me.dblab.common.values.*;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Type implements Serializable {
    private static HashMap<String, String> ExistingTypes(){
        return new HashMap<String, String>(){
            {
                put("Int",      "me.dblab.common.values.IntType");
                put("Double",   "me.dblab.common.values.DoubleType");
                put("Char",     "me.dblab.common.values.CharType");
                put("Long",     "me.dblab.common.values.LongType");
                put("String",   "me.dblab.common.values.StringType");
                put("HTML",     "me.dblab.common.values.HTMLType");
                put("Date",     "me.dblab.common.values.DateType");
            }
        };
    };


    public abstract boolean supports(byte[] value);

    public abstract String toString(byte[] value);

    public Value createWithValue(byte[] value) throws WrongBytesArrayException {
        return new Value(this, value);
    }

    public static Type typeFromString(String type) throws InvalidTypeException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        if(ExistingTypes().containsKey(type)) {
            System.out.println(ExistingTypes().get(type));

            Class cls = Class.forName(ExistingTypes().get(type));
            return (Type) cls.newInstance();
        }
        else {
            throw new InvalidTypeException();
        }
    }

    public abstract byte[] fromString(String s) throws StringNotSupportedForTypeException;

    public boolean equals(Object o) {
        return this == o || (o != null && getClass() == o.getClass());
    }
}
