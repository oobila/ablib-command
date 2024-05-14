package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

public class Argument<T> extends ArgumentBase<T, Argument<T>> {
    public Argument(String name, Class<T> type, ArgumentDeserializer<T> deserializer) {
        super(name, type, deserializer);
    }
}
