package me.dblab.common.values;

import me.dblab.exceptions.InvalidTypeException;
import me.dblab.exceptions.StringNotSupportedForTypeException;
import me.dblab.exceptions.WrongBytesArrayException;

import java.io.Serializable;
import java.util.HashMap;

public abstract class Type implements Serializable {
    private static HashMap<String, String> ExistingTypes(){
        return new HashMap<String, String>(){
            {
                put("Int","IntType");
                put("Double","DoubleType");
                put("Char","CharType");
                put("Long","LongType");
                put("String","StringType");
                put("HTML","HTMLType");
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
