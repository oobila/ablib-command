package com.github.oobila.bukkit.command;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@SuppressWarnings("unchecked")
public class ArgumentBase<T, S extends ArgumentBase<T,?>> {

    private final String name;
    private final Class<T> type;
    private boolean mandatory = true;
    private List<T> fixedSuggestions;
    private SuggestionCallable<T> suggestionCallable;
    private T min;
    private T max;
    private T defaultValue;
    private final ArgumentDeserializer<T> deserializer;
    int position;

    @SuppressWarnings("unchecked")
    public ArgumentBase(String name, Class<T> type, ArgumentDeserializer<T> deserializer) {
        this.name = name;
        this.type = type;
        this.deserializer = deserializer;
        if (type.isEnum()) {
            this.fixedSuggestions = Arrays.stream(type.getEnumConstants()).toList();
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            this.fixedSuggestions = (List<T>) List.of(true, false);
        }
    }

    public S mandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return (S) this;
    }

    public S defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return (S) this;
    }

    public S fixedSuggestions(List<T> fixedSuggestions) {
        this.suggestionCallable = null;
        this.fixedSuggestions = fixedSuggestions;
        return (S) this;
    }

    public S suggestionCallable(SuggestionCallable<T> suggestionCallable) {
        this.fixedSuggestions = null;
        this.suggestionCallable = suggestionCallable;
        return (S) this;
    }

    public S range(T min, T max) {
        this.min = min;
        this.max = max;
        return (S) this;
    }

    public void validate(List<ArgumentBase<?,?>> arguments) {
        if (!arguments.isEmpty() && !arguments.get(arguments.size() - 1).isMandatory() && mandatory) {
            throw new RuntimeException("Mandatory arguments must exist at the end of the command");
        }
    }

    public T get(String[] args) {
        if (args.length > position) {
            return deserializer.deserialize(args[position]);
        }
        return defaultValue;
    }

    public interface ArgumentDeserializer<T> {
        T deserialize(String arg);
    }
}
