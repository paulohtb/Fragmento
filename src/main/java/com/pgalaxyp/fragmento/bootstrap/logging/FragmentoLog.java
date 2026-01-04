package com.pgalaxyp.fragmento.bootstrap.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.function.Consumer;

public final class FragmentoLog {

    private static final int MAX_QUEUE = 1024;
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
            runtime("file logging started, file={}", logFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[FragmentoLog] Falha ao iniciar log em arquivo: " + e.getMessage());
        }
    }

    private static void log(String tag, String message) {
        if (tag == null || message == null) return;

        String line = "[" + FORMAT.format(LocalDateTime.now()) + "] [" + tag + "] " + message;
        QUEUE.offer(line);

        if (writer != null) {
            FILE_WRITER.submit(() -> {
                try {
                    writer.write(line);
                    writer.newLine();
                    writer.flush();
                } catch (IOException ignored) {}
            });
        }
    }

    private static void logEx(String tag, Throwable t, String message, Object... args) {
        log(tag, format(message, args));
        if (t == null) return;

        log(tag, "exception.class=" + t.getClass().getName());
        if (t.getMessage() != null) log(tag, "exception.message=" + t.getMessage());

        StackTraceElement[] st = t.getStackTrace();
        if (st != null) {
            int max = Math.min(6, st.length);
            for (int i = 0; i < max; i++) {
                log(tag, "at " + st[i]);
            }
        }
    }

    public static void flushTo(Consumer<String> sink) {
        if (sink == null) return;
        String line;
        while ((line = QUEUE.poll()) != null) sink.accept(line);
    }

    private static String format(String msg, Object... args) {
        if (msg == null) return "";
        if (args == null || args.length == 0) return msg;
        return String.format(msg.replace("{}", "%s"), args);
    }

    public static void runtime(String msg, Object... args) { log("RUNTIME", format(msg, args)); }
    public static void combat(String msg, Object... args) { log("COMBAT", format(msg, args)); }
    public static void intent(String msg, Object... args) { log("INTENT", format(msg, args)); }
    public static void network(String msg, Object... args) { log("NET", format(msg, args)); }
    public static void snapshot(String msg, Object... args) { log("SNAPSHOT", format(msg, args)); }
    public static void inventory(String msg, Object... args) { log("INV", format(msg, args)); }

    public static void runtimeEx(Throwable t, String msg, Object... args) { logEx("RUNTIME", t, msg, args); }
    public static void combatEx(Throwable t, String msg, Object... args) { logEx("COMBAT", t, msg, args); }
    public static void intentEx(Throwable t, String msg, Object... args) { logEx("INTENT", t, msg, args); }
    public static void networkEx(Throwable t, String msg, Object... args) { logEx("NET", t, msg, args); }
    public static void snapshotEx(Throwable t, String msg, Object... args) { logEx("SNAPSHOT", t, msg, args); }
    public static void inventoryEx(Throwable t, String msg, Object... args) { logEx("INV", t, msg, args); }

    private FragmentoLog() {}
}