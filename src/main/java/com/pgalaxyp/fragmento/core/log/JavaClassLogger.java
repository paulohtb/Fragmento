package com.pgalaxyp.fragmento.core.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class JavaClassLogger {

    private static final Path BASE_DIR =
            Paths.get("C:\\Users\\User\\Documents\\Projetos\\Fragmento\\src\\main\\java");

    private static final List<String> TARGET_FOLDERS = List.of(
            "fragmento/client",
            "fragmento/content",
            "fragmento/network",
            "fragmento/system"
    );

    private static final String OUTPUT_FILE_NAME = "java-classes-log.txt";

    public static void main(String[] args) throws IOException {

        Path logDir = BASE_DIR
                .resolve("com")
                .resolve("pgalaxyp")
                .resolve("fragmento")
                .resolve("core")
                .resolve("log");

        Path outputFile = logDir.resolve(OUTPUT_FILE_NAME);

        List<String> outputLines = new ArrayList<>();

        for (String folderName : TARGET_FOLDERS) {
            Path targetDir = BASE_DIR
                    .resolve("com")
                    .resolve("pgalaxyp")
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
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(javaFile -> processJavaFile(javaFile, outputLines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processJavaFile(Path javaFile, List<String> outputLines) {
        outputLines.add("===== " + javaFile.getFileName() + " =====");

        try {
            Files.readAllLines(javaFile, StandardCharsets.UTF_8).stream()
                    .filter(line -> !line.startsWith("import "))
                    .forEach(outputLines::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        outputLines.add("");
    }
}
