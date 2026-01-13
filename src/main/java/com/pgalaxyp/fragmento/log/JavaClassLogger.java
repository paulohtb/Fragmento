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
            "java/com/pgalaxyp/fragmento/rpg"
    );

    private static final String OUTPUT_FILE_NAME = "java-classes-log.txt";

    public static void main(String[] args) throws IOException {

        Path logDir = BASE_DIR
                .resolve("java/com/pgalaxyp/fragmento/log");

        Files.createDirectories(logDir);

        Path outputFile = logDir.resolve(OUTPUT_FILE_NAME);
        List<String> outputLines = new ArrayList<>();

        for (String folder : TARGET_FOLDERS) {
            Path targetDir = BASE_DIR.resolve(folder);

            if (Files.isDirectory(targetDir)) {
                collectFilesRecursively(targetDir, outputLines);
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

    private static void collectFilesRecursively(Path root, List<String> outputLines) {
        try (Stream<Path> paths = Files.walk(root)) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(JavaClassLogger::isTargetFile)
                    .sorted()
                    .forEach(file -> appendFile(file, root, outputLines));

        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer diretório: " + root, e);
        }
    }

    private static boolean isTargetFile(Path path) {
        return true;
    }

    private static void appendFile(Path file, Path root, List<String> outputLines) {
        Path relativePath = root.relativize(file);
        outputLines.add("===== " + relativePath + " =====");

        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            lines.forEach(outputLines::add);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + file, e);
        }

        outputLines.add("");
    }
}