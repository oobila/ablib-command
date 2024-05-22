package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@SuppressWarnings("unchecked")
public class EnumArg<T extends Enum<T>> extends ArgumentBase<T, EnumArg<T>> {
    public EnumArg(String name, Class<T> type) {
        super(name, type, arg -> Enum.valueOf(type, arg));
        try {
            fixedSuggestions((List<T>) type.getDeclaredMethod("valueOf", String.class).invoke(null, type));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
