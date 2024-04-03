package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.Argument;

public class StringArg extends Argument<String> {
    public StringArg(String name) {
        super(name, String.class, String::toString);
    }
}
