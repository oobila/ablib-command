package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.util.List;

public class BooleanArg extends ArgumentBase<Boolean, BooleanArg> {
    public BooleanArg(String name) {
        super(name, boolean.class, Boolean::parseBoolean);
        fixedSuggestions(List.of(true, false));
        defaultValue(false);
    }
}
