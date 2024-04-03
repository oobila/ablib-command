package com.github.oobila.bukkit.command.validators;

import com.github.oobila.bukkit.chat.Message;
import org.apache.commons.lang3.math.NumberUtils;

import static com.github.oobila.bukkit.command.validators.ValidationResponse.failed;
import static com.github.oobila.bukkit.command.validators.ValidationResponse.passed;

public class NumericValidator implements Validator {

    private static final String INPUT_IS_NOT_A_NUMBER = "argument: [{0}] is not a number";
    private static final String INPUT_IS_OUT_OF_BOUNDS = "argument: [{0}] is outside of it's bounds";

    @Override
    public <T> ValidationResponse isValid(String input, Class<T> type) {
        return NumberUtils.isNumber(input) ? passed() : failed(Message.builder(INPUT_IS_NOT_A_NUMBER).arg(input));
    }

    @Override
    public <T> ValidationResponse isValid(String input, Class<T> type, Object min, Object max) {
        boolean result = false;
        if (type.equals(int.class) || type.equals(Integer.class)) {
            int value = Integer.parseInt(input);
            result = value >= (int) min && value <= (int) max;
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            float value = Float.parseFloat(input);
            result = value >= (float) min && value <= (float) max;
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            long value = Long.parseLong(input);
            result = value >= (long) min && value <= (long) max;
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            double value = Double.parseDouble(input);
            result = value >= (double) min && value <= (double) max;
        }
        return result ? passed() : failed(Message.builder(INPUT_IS_OUT_OF_BOUNDS).arg(input));
    }
}
