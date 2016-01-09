package me.dblab.common;

import me.dblab.common.values.Type;

public class Column {
    private final String name;
    private final Type type;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
