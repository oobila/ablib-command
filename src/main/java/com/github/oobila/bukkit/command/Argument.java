package com.github.oobila.bukkit.command;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Argument<T> {

    private final String name;
    private final Class<T> type;
    private boolean mandatory = true;
    private List<T> fixedSuggestions;
    private SuggestionCallable<T> suggestionCallable;
    private T min;
    private T max;
    private final ArgumentDeserializer<T> deserializer;
    int position;

    @SuppressWarnings("unchecked")
    public Argument(String name, Class<T> type, ArgumentDeserializer<T> deserializer) {
        this.name = name;
        this.type = type;
        this.deserializer = deserializer;
        if (type.isEnum()) {
            this.fixedSuggestions = Arrays.stream(type.getEnumConstants()).toList();
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            this.fixedSuggestions = (List<T>) List.of(true, false);
        }
    }

    public static <T> Argument<T> arg(String name, Class<T> type, ArgumentDeserializer<T> deserializer) {
        return new Argument<>(name, type, deserializer);
    }

    public Argument<T> mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public Argument<T> fixedSuggestions(List<T> fixedSuggestions) {
        this.suggestionCallable = null;
        this.fixedSuggestions = fixedSuggestions;
        return this;
    }

    public Argument<T> suggestionCallable(SuggestionCallable<T> suggestionCallable) {
        this.fixedSuggestions = null;
        this.suggestionCallable = suggestionCallable;
        return this;
    }

    public Argument<T> range(T min, T max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public void validate(List<Argument<?>> arguments) {
        if (!arguments.isEmpty() && !arguments.get(arguments.size() - 1).isMandatory() && mandatory) {
            throw new RuntimeException("Mandatory arguments must exist at the end of the command");
        }
    }

    public T get(String[] args) {
        return deserializer.deserialize(args[position]);
    }

    public interface ArgumentDeserializer<T> {
        T deserialize(String arg);
    }
}
