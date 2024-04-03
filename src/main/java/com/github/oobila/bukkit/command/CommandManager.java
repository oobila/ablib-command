package com.github.oobila.bukkit.command;

import com.github.oobila.bukkit.command.validators.EnumValidator;
import com.github.oobila.bukkit.command.validators.NumericValidator;
import com.github.oobila.bukkit.command.validators.OfflinePlayerValidator;
import com.github.oobila.bukkit.command.validators.PlayerValidator;
import com.github.oobila.bukkit.command.validators.Validator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandManager {

    @Getter(AccessLevel.PACKAGE)
    private static final Validator enumValidator = new EnumValidator<>();
    @Getter(AccessLevel.PACKAGE)
    private static final Map<Class<?>, Validator> validators = new HashMap<>();
    static {
        NumericValidator numericValidator = new NumericValidator();
        validators.put(int.class, numericValidator);
        validators.put(Integer.class, numericValidator);
        validators.put(float.class, numericValidator);
        validators.put(Float.class, numericValidator);
        validators.put(long.class, numericValidator);
        validators.put(Long.class, numericValidator);
        validators.put(double.class, numericValidator);
        validators.put(Double.class, numericValidator);
        validators.put(Player.class, new PlayerValidator());
        validators.put(OfflinePlayer.class, new OfflinePlayerValidator());
    }

    public void addValidator(Class<?> type, Validator validator) {
        validators.put(type, validator);
    }

}
