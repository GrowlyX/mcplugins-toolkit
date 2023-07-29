package io.liftgate.mcplugins.toolkit.utilities;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author GrowlyX
 * @since 7/28/2023
 */
public interface SafeConsumer<T> extends Consumer<T> {
    default void acceptSafely(@NotNull T t) {
        try {
            accept(t);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    default void acceptSafely(
            @NotNull T t,
            @NotNull Consumer<Throwable> consumer
    ) {
        try {
            accept(t);
        } catch (Throwable throwable) {
            consumer.accept(throwable);
        }
    }
}
