package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.Argument;

public class DoubleArg extends Argument<Double> {
    public DoubleArg(String name) {
        super(name, double.class, Double::parseDouble);
    }
}
