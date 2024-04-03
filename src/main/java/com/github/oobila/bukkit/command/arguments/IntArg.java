package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.Argument;

public class IntArg extends Argument<Integer> {
    public IntArg(String name) {
        super(name, int.class, Integer::parseInt);
    }
}
