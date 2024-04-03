package com.github.oobila.bukkit.command.validators;

import com.github.oobila.bukkit.chat.Message;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.oobila.bukkit.command.validators.ValidationResponse.failed;
import static com.github.oobila.bukkit.command.validators.ValidationResponse.passed;

public class EnumValidator<E extends Enum<E>> implements Validator {

    private static final String INPUT_IS_NOT_OPTION = "argument: [{0}] is not a valid option. Options:\n{1}";

    @Override
    @SuppressWarnings("unchecked")
    public <T> ValidationResponse isValid(String input, Class<T> type) {
        Class<E> enumClass = (Class<E>) type;
        try {
            Enum.valueOf(enumClass, input);
            return passed();
        } catch (IllegalArgumentException exception) {
            String values = EnumSet.allOf(enumClass).stream().map(
                    e -> " - " + e.toString()).collect(Collectors.joining("\n")
            );
            return failed(Message.builder(INPUT_IS_NOT_OPTION)
                    .arg(input)
                    .arg(values)
            );
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ValidationResponse isValid(String input, Class<T> type, Object min, Object max) {
        Class<E> enumClass = (Class<E>) type;
        try {
            T value = (T) Enum.valueOf(enumClass, input);
            Set<E> validValues = validValues(enumClass, (E) min, (E) max);
            if (validValues.contains((E) value)) {
                return passed();
            } else {
                String values = validValues.stream().map(
                        e -> " - " + e.toString()).collect(Collectors.joining("\n")
                );
                return failed(Message.builder(INPUT_IS_NOT_OPTION)
                        .arg(input)
                        .arg(values)
                );
            }
        } catch (IllegalArgumentException exception) {
            String values = EnumSet.allOf(enumClass).stream().map(
                    e -> " - " + e.toString()).collect(Collectors.joining("\n")
            );
            return failed(Message.builder(INPUT_IS_NOT_OPTION)
                    .arg(input)
                    .arg(values)
            );
        }
    }

    private Set<E> validValues(Class<E> enumClass, E min, E max) {
        EnumSet<E> enumSet = EnumSet.allOf(enumClass);
        boolean enabled = false;
        Set<E> validValues = new HashSet<>();
        for (E e : enumSet) {
            if (e.equals(min)) {
                enabled = true;
            } else if (e.equals(max)) {
                break;
            }
            if (enabled) {
                validValues.add(e);
            }
        }
        return validValues;
    }
}
