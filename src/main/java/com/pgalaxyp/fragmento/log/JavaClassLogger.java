package com.pgalaxyp.fragmento.log;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public final class JavaClassLogger {
    private static final Path TARGET_DIR = Paths.get("src", "main", "java", "com", "pgalaxyp", "fragmento", "combat");
    private static final Path LOG_DIR = Paths.get("src", "main", "java", "com", "pgalaxyp", "fragmento", "log");
    private static final Path OUTPUT_FILE = LOG_DIR.resolve("java-classes-log.txt");

    public static void main(String[] args) throws IOException {
        Files.createDirectories(LOG_DIR);
        List<String> lines = new ArrayList<>();
        try (Stream<Path> files = Files.walk(TARGET_DIR)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java")).sorted()
                    .forEach(file -> {
                        try {
                            lines.addAll(Files.readAllLines(file, StandardCharsets.UTF_8));
                            lines.add("");
                        } catch (IOException e) { throw new RuntimeException(e); }
                    });
        }
        Files.write(OUTPUT_FILE, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}