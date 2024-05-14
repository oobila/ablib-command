package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

public class DoubleArg extends ArgumentBase<Double, DoubleArg> {
    public DoubleArg(String name) {
        super(name, double.class, Double::parseDouble);
    }
}
