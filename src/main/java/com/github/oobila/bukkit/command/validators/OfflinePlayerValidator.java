package com.github.oobila.bukkit.command.validators;

import com.github.oobila.bukkit.chat.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import static com.github.oobila.bukkit.command.validators.ValidationResponse.failed;
import static com.github.oobila.bukkit.command.validators.ValidationResponse.passed;

public class OfflinePlayerValidator implements Validator {

    private static final String INPUT_IS_NOT_PLAYER = "argument: [{0}] is not a player on this server";

    @Override
    @SuppressWarnings("deprecated")
    public <T> ValidationResponse isValid(String input, Class<T> type) {
        try {
            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                if (player.getName().equalsIgnoreCase(input)) {
                    return passed();
                }
            }
            return failed(Message.builder(INPUT_IS_NOT_PLAYER).arg(input));
        } catch (Exception e) {
            //do nothing
        }
        return failed(Message.builder(INPUT_IS_NOT_PLAYER).arg(input));
    }

    @Override
    public <T> ValidationResponse isValid(String input, Class<T> type, Object min, Object max) {
        return isValid(input, type);
    }
}
