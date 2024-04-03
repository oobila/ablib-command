package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.Argument;

public class EnumArg<T extends Enum<T>> extends Argument<T> {
    public EnumArg(String name, Class<T> type) {
        super(name, type, arg -> Enum.valueOf(type, arg));
    }
}
