package com.github.oobila.bukkit.command.validators;

public interface Validator {

    <T> ValidationResponse isValid(String input, Class<T> type);

    <T> ValidationResponse isValid(String input, Class<T> type, Object min, Object max);

}
