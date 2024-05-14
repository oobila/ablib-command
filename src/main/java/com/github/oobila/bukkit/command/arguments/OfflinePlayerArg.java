package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfflinePlayerArg extends ArgumentBase<OfflinePlayer, OfflinePlayerArg> {
    public OfflinePlayerArg(String name) {
        super(name, OfflinePlayer.class, Bukkit::getOfflinePlayer);
    }
}
