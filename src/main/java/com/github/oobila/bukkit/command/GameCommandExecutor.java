package com.github.oobila.bukkit.command;

public interface GameCommandExecutor {

    void onCommand(Command command, String label, String[] args);

}
