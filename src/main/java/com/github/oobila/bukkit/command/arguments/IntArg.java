package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;

import java.util.List;

public class IntArg extends ArgumentBase<Integer, IntArg> {
    public IntArg(String name) {
        super(name, int.class, Integer::parseInt);
        fixedSuggestions(List.of(1));
    }
}
