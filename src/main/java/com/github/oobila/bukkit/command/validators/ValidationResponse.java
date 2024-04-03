package com.github.oobila.bukkit.command.validators;

import com.github.oobila.bukkit.chat.Message;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ValidationResponse {
    @Getter(AccessLevel.NONE)
    private final boolean result;
    private final Message message;

    private ValidationResponse(boolean result, Message message) {
        this.result = result;
        this.message = message;
    }

    public static ValidationResponse passed() {
        return new ValidationResponse(true, null);
    }

    public static ValidationResponse failed(Message message) {
        return new ValidationResponse(false, message);
    }

    public boolean getResult() {
        return result;
    }
}
