package com.github.oobila.bukkit.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoolStatement {

    private boolean result;

    boolean onTrue(Runnable r) {
        if (result) {
            r.run();
        }
        return result;
    }

    boolean onFalse(Runnable r) {
        if (!result) {
            r.run();
        }
        return result;
    }

    static BoolStatement bool(boolean b) {
        return new BoolStatement(b);
    }

}
