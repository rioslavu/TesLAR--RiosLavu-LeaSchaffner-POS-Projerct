package school.tesla.value;

import java.util.Objects;
import java.util.regex.Pattern;

final class ValueChecks {
    private ValueChecks() {
    }

    static void length(String name, String value, int max) {
        Objects.requireNonNull(value, name);
        if (value.length() > max) {
            throw new IllegalArgumentException(name + " must have at most " + max + " characters");
        }
    }

    static void matches(String name, String value, Pattern pattern) {
        Objects.requireNonNull(value, name);
        if (!pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(name + " must match " + pattern.pattern());
        }
    }

    static void range(String name, int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be between " + min + " and " + max);
        }
    }

    static void range(String name, double value, double min, double max) {
        if (!Double.isFinite(value) || value < min || value > max) {
            throw new IllegalArgumentException(name + " must be between " + min + " and " + max);
        }
    }
}
