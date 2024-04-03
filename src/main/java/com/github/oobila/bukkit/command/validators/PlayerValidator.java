package com.github.oobila.bukkit.command.validators;

import com.github.oobila.bukkit.chat.Message;
import org.bukkit.Bukkit;

import static com.github.oobila.bukkit.command.validators.ValidationResponse.failed;
import static com.github.oobila.bukkit.command.validators.ValidationResponse.passed;

public class PlayerValidator implements Validator {

    private static final String INPUT_IS_NOT_PLAYER = "argument: [{0}] is not an online player";

    @Override
    public <T> ValidationResponse isValid(String input, Class<T> type) {
        try {
            if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(input))) {
                return passed();
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
