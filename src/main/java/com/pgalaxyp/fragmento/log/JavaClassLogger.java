package com.pgalaxyp.fragmento.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class JavaClassLogger {

    private static final Path BASE_DIR =
            Paths.get("C:\\Users\\Loteria Aldeota\\Documents\\projetos\\analise f\\src\\main");

    private static final List<String> TARGET_FOLDERS = List.of(
            "C:\\Users\\Loteria Aldeota\\Documents\\projetos\\analise f\\src\\main\\java\\com\\pgalaxyp\\fragmento\\combat"
    );

    private static final String OUTPUT_FILE_NAME = "java-classes-log.txt";

    public static void main(String[] args) throws IOException {

        System.out.println("Iniciando JavaClassLogger");
        System.out.println("BASE_DIR = " + BASE_DIR);
        System.out.println("BASE_DIR existe: " + Files.exists(BASE_DIR));
        System.out.println("BASE_DIR é diretório: " + Files.isDirectory(BASE_DIR));

        Path logDir = BASE_DIR.resolve("java/com/pgalaxyp/fragmento/log");
        System.out.println("LOG_DIR = " + logDir);

        Files.createDirectories(logDir);

        Path outputFile = logDir.resolve(OUTPUT_FILE_NAME);
        System.out.println("Arquivo de saída = " + outputFile);

        List<String> outputLines = new ArrayList<>();

        for (String folder : TARGET_FOLDERS) {
            Path targetDir = BASE_DIR.resolve(folder);

            System.out.println("Verificando diretório alvo: " + targetDir);
            System.out.println("Existe: " + Files.exists(targetDir));
            System.out.println("É diretório: " + Files.isDirectory(targetDir));

            if (Files.isDirectory(targetDir)) {
                System.out.println("Entrando no diretório: " + targetDir);
                collectFilesRecursively(targetDir, outputLines);
            } else {
                System.out.println("Diretório ignorado");
            }
        }

        System.out.println("Total de linhas coletadas: " + outputLines.size());

        Files.write(
                outputFile,
                outputLines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        System.out.println("Finalizado com sucesso");
    }

    private static void collectFilesRecursively(Path root, List<String> outputLines) {
        try (Stream<Path> paths = Files.walk(root)) {
            paths
                    .filter(Files::isRegularFile)
                    .sorted()
                    .forEach(file -> {
                        System.out.println("Lendo arquivo: " + file);
                        appendFile(file, root, outputLines);
                    });

        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer diretório: " + root, e);
        }
    }

    private static void appendFile(Path file, Path root, List<String> outputLines) {
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            lines.forEach(outputLines::add);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo: " + file, e);
        }

        outputLines.add("");
    }
}