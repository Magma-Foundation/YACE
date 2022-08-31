package yace;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Represents an object which offers passage between code and an external I/O system.
 */
public interface Gateway {
    /**
     * Streams all the sources present.
     *
     * @return The sources.
     */
    Stream<Path> streamSources() throws IOException;
}
