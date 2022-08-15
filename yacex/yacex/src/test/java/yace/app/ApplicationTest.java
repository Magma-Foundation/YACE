package yace.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yace.CompileException;
import yace.MismatchException;
import yace.io.Path;
import yace.io.VirtualFileSystem;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    private Path source;
    private Path target;

    @BeforeEach
    void setUp() {
        var system = new VirtualFileSystem();
        source = system.resolve("Index.java");
        target = system.resolve("Index.mgs");
    }

    @Test
    void content() throws IOException {
        writeSource("class Index {}");
        runEmpty();
        assertTrue(target.exists());
    }

    @Test
    void different_class_name() throws IOException {
        writeSource("class Test {}");
        assertThrows(MismatchException.class, this::runEmpty);
    }

    private void runEmpty() {
        new Application(source, target).run(Optional.empty(), false);
    }

    @Test
    void task_no_target() throws IOException {
        rename(false);
        assertFalse(target.exists());
    }

    @Test
    void rename_class() throws IOException {
        rename(false);
        assertEquals("class Bar {}", source.existingAsFile().orElseThrow()
                .readAsString());
    }

    @Test
    void rename_preview() throws IOException {
        var output = renamePreviewImpl();
        assertEquals("class Bar {}", output);
    }

    @Test
    void rename_preview_file_not_change() throws IOException {
        renamePreviewImpl();
        assertEquals("class Foo {}", source.existingAsFile()
                .orElseThrow()
                .readAsString());
    }

    private String renamePreviewImpl() throws IOException {
        return rename(true).orElseThrow();
    }

    private Optional<String> rename(boolean shouldPreview) throws IOException {
        writeSource("class Foo {}");
        return new Application(source, target).run(Optional.of("Bar"), shouldPreview);
    }

    private void writeSource(String content) throws IOException {
        source.createAsFile().writeAsString(content);
    }

    @Test
    void empty() throws IOException {
        source.createAsFile();
        assertThrows(CompileException.class, this::runEmpty);
    }
}