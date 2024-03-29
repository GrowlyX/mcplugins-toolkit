package io.liftgate.mcplugins.toolkit.utilities;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author GrowlyX
 * @since 7/28/2023
 */
@Builder
@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor
public class FileWatcherObject {
    private final @NotNull File file;
    private final @NotNull SafeConsumer<File> consumer;

    @Setter
    @Builder.Default
    private @NotNull Long lastEditedTimestamp = -1L;
}
