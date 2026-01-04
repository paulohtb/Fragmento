package com.pgalaxyp.fragmento.bootstrap.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class FragmentoLog {

    private static final int MAX_QUEUE = 4096;
    private static final BlockingQueue<String> QUEUE = new ArrayBlockingQueue<>(MAX_QUEUE);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static volatile BufferedWriter writer;

    private static final ExecutorService FILE_WRITER = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "FragmentoLogWriter");
        t.setDaemon(true);
        return t;
    });

    public static void initFileLogging() {
        try {
            File logDir = new File("logs/fragmento");
            if (!logDir.exists()) Files.createDirectories(logDir.toPath());

            String fileName = "fragmento-" + LocalDate.now() + ".log";
            File logFile = new File(logDir, fileName);

            writer = new BufferedWriter(new FileWriter(logFile, true));
            log(LogChannel.RUNTIME, "file logging started, file={}", logFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[FragmentoLog] Falha ao iniciar log em arquivo: " + e.getMessage());
        }
    }

    public static void log(LogChannel channel, String message, Object... args) {
        if (channel == null || message == null) return;

        String line = "[" + FORMAT.format(LocalDateTime.now()) + "] [" + channel.name() + "] " + format(message, args);
        QUEUE.offer(line);

        BufferedWriter w = writer;
        if (w != null) {
            FILE_WRITER.submit(() -> {
                try {
                    w.write(line);
                    w.newLine();
                    w.flush();
                } catch (IOException ignored) {
                }
            });
        }
    }

    public static void logEx(LogChannel channel, Throwable t, String message, Object... args) {
        log(channel, message, args);
        if (channel == null || t == null) return;

        log(channel, "exception.class={}", t.getClass().getName());
        if (t.getMessage() != null) log(channel, "exception.message={}", t.getMessage());

        StackTraceElement[] st = t.getStackTrace();
        if (st == null) return;

        int max = Math.min(8, st.length);
        for (int i = 0; i < max; i++) {
            log(channel, "at {}", st[i]);
        }
    }

    public static void flushTo(Consumer<String> sink) {
        if (sink == null) return;
        String line;
        while ((line = QUEUE.poll()) != null) {
            sink.accept(line);
        }
    }

    private static String format(String msg, Object... args) {
        if (msg == null) return "";
        if (args == null || args.length == 0) return msg;
        String fmt = msg.replace("{}", "%s");
        Object[] safeArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            safeArgs[i] = Objects.toString(args[i]);
        }
        return String.format(fmt, safeArgs);
    }

    private FragmentoLog() {}
}