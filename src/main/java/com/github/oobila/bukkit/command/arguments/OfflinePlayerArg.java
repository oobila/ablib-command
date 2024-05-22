package com.github.oobila.bukkit.command.arguments;

import com.github.oobila.bukkit.command.ArgumentBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;

public class OfflinePlayerArg extends ArgumentBase<OfflinePlayer, OfflinePlayerArg> {
    public OfflinePlayerArg(String name) {
        super(name, OfflinePlayer.class, Bukkit::getOfflinePlayer);
        suggestionCallable((player, arg) -> {
            if (arg == null || arg.isEmpty()) {
                return List.of(player);
            } else {
                return Arrays.stream(Bukkit.getOfflinePlayers())
                        .filter(offlinePlayer -> offlinePlayer.getName().toLowerCase().contains(arg.toLowerCase()))
                        .limit(20)
                        .toList();
            }
        });
    }
}
