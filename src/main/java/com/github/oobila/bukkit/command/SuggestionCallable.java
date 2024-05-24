package com.github.oobila.bukkit.command;

import org.bukkit.entity.Player;

import java.util.List;

public interface SuggestionCallable {

    List<String> getSuggestions(Player player, String arg);

}
