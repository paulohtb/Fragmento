package com.pgalaxyp.fragmento.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class JavaClassLogger {

    private static final Path BASE_DIR =
            Paths.get("C:\\Users\\User\\Documents\\Projetos\\Fragmento\\src\\main");

    private static final List<String> TARGET_FOLDERS = List.of(
            "java/com/pgalaxyp/fragmento/bootstrap",
            "java/com/pgalaxyp/fragmento/rpg"
    );

    private static final String OUTPUT_FILE_NAME = "java-classes-log.txt";

    public static void main(String[] args) throws IOException {

        Path logDir = BASE_DIR
                .resolve("java")
                .resolve("com")
                .resolve("pgalaxyp")
                .resolve("fragmento")
                .resolve("log");

        Path outputFile = logDir.resolve(OUTPUT_FILE_NAME);

        List<String> outputLines = new ArrayList<>();

        for (String folderName : TARGET_FOLDERS) {
            Path targetDir = BASE_DIR
                    .resolve(folderName);

            if (Files.isDirectory(targetDir)) {
                collectJavaFiles(targetDir, outputLines);
            }
        }

        Files.write(
                outputFile,
                outputLines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    private static void collectJavaFiles(Path directory, List<String> outputLines) {
        try (Stream<Path> files = Files.walk(directory)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(p ->
                            p.toString().endsWith(".java") ||
                                    p.toString().endsWith(".json")
                    )
                    .forEach(file -> processFile(file, outputLines));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processFile(Path file, List<String> outputLines) {
        outputLines.add("===== " + file.getFileName() + " =====");

        try {
            outputLines.addAll(Files.readAllLines(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        outputLines.add("");
    }
}