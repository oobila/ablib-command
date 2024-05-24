package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.util.Arrays;

public class EnumArg<T extends Enum<T>> extends ArgumentBase<T, EnumArg<T>> {
    public EnumArg(String name, Class<T> type) {
        super(name, type, arg -> Enum.valueOf(type, arg));
        fixedSuggestions(Arrays.stream(type.getEnumConstants()).map(T::toString).toList());
    }
}
