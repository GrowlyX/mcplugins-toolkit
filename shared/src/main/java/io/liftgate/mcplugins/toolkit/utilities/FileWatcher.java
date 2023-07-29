package io.liftgate.mcplugins.toolkit.utilities;

import io.liftgate.mcplugins.toolkit.ToolkitPlugin;
import io.liftgate.mcplugins.toolkit.contracts.Eager;
import io.liftgate.mcplugins.toolkit.export.Export;
import jakarta.inject.Inject;
import org.glassfish.hk2.api.PostConstruct;
import org.glassfish.hk2.api.PreDestroy;
import org.jetbrains.annotations.NotNull;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Listens and responds to file changes.
 *
 * @author GrowlyX
 * @since 7/28/2023
 */
@Export
@Service
public class FileWatcher implements PostConstruct, Runnable, PreDestroy, Eager {
    private final ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();

    private final List<FileWatcherObject> watchedObjects = new ArrayList<>();

    @Inject
    public ToolkitPlugin plugin;

    /**
     * Add a FileWatcherObject to our list of watched objects.
     */
    public void watch(
            @NotNull
            final FileWatcherObject object
    ) {
        synchronized (this.watchedObjects) {
            this.watchedObjects.add(object);
        }
    }

    @Override
    public void run() {
        synchronized (this.watchedObjects) {
            for (var watchedObject : this.watchedObjects) {
                var lastModification = watchedObject.getFile().lastModified();

                if (lastModification != watchedObject.getLastEditedTimestamp()) {
                    watchedObject.setLastEditedTimestamp(lastModification);

                    watchedObject.getConsumer().acceptSafely(
                            watchedObject.getFile(),
                            throwable -> plugin.getLogger().log(
                                    Level.WARNING,
                                    "FileWatcher was unable to perform change updates on " + watchedObject.getFile().getName(),
                                    throwable
                            )
                    );
                }
            }
        }
    }

    @Override
    public void postConstruct() {
        this.executorService.schedule(this, 1L, TimeUnit.SECONDS);
    }

    @Override
    public void preDestroy() {
        this.executorService.shutdownNow();
    }
}
