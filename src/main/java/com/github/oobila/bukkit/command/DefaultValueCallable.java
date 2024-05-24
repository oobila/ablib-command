package com.github.oobila.bukkit.command;

import org.bukkit.entity.Player;

public interface DefaultValueCallable<T> {

    T getDefaultValue(Player player, String arg);

}
