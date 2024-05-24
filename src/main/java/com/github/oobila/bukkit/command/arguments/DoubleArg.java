package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.util.List;

public class DoubleArg extends ArgumentBase<Double, DoubleArg> {
    public DoubleArg(String name) {
        super(name, double.class, Double::parseDouble);
        fixedSuggestions(List.of("1.0"));
        defaultValue(0d);
    }
}
