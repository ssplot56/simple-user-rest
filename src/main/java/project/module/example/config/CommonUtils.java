package project.module.example.config;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CommonUtils {

    public static class If {
        public static <T> void setIfNotNull(T value, Consumer<T> setter) {
            setIf(Objects::nonNull, () -> value, setter);
        }

        public static <T, E extends T> void setIf(Predicate<T> condition, Supplier<E> getter, Consumer<T> setter) {
            if (condition.test(getter.get())) {
                setter.accept(getter.get());
            }
        }
    }

}
