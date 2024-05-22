package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.util.List;

public class StringArg extends ArgumentBase<String, StringArg> {
    public StringArg(String name) {
        super(name, String.class, String::toString);
        fixedSuggestions(List.of("?"));
    }
}
