package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.Argument;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerArg extends Argument<OfflinePlayer> {
    public OfflinePlayerArg(String name) {
        super(name, OfflinePlayer.class, Bukkit::getOfflinePlayer);
    }
}
