package com.pgalaxyp.fragmento.bootstrap.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public final class FragmentoLog {

    private static final int MAX_QUEUE = 1024;

    private static final BlockingQueue<String> QUEUE =
            new ArrayBlockingQueue<>(MAX_QUEUE);

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void runtime(String message, Object... args) {
        log("RUNTIME", format(message, args));
    }

    public static void combat(String message, Object... args) {
        log("COMBAT", format(message, args));
    }

    public static void intent(String message, Object... args) {
        log("INTENT", format(message, args));
    }

    public static void network(String message, Object... args) {
        log("NET", format(message, args));
    }

    public static void snapshot(String message, Object... args) {
        log("SNAPSHOT", format(message, args));
    }

    public static void inventory(String message, Object... args) {
        log("INV", format(message, args));
    }

    public static void runtimeEx(Throwable t, String message, Object... args) {
        logEx("RUNTIME", t, message, args);
    }

    public static void combatEx(Throwable t, String message, Object... args) {
        logEx("COMBAT", t, message, args);
    }

    public static void intentEx(Throwable t, String message, Object... args) {
        logEx("INTENT", t, message, args);
    }

    public static void networkEx(Throwable t, String message, Object... args) {
        logEx("NET", t, message, args);
    }

    public static void snapshotEx(Throwable t, String message, Object... args) {
        logEx("SNAPSHOT", t, message, args);
    }

    public static void inventoryEx(Throwable t, String message, Object... args) {
        logEx("INV", t, message, args);
    }

    private static void log(String tag, String message) {
        if (tag == null || message == null) {
            return;
        }

        String line =
                "[" + FORMAT.format(LocalDateTime.now()) + "] "
                        + "[" + tag + "] "
                        + message;

        QUEUE.offer(line);
    }

    private static void logEx(String tag, Throwable t, String message, Object... args) {
        log(tag, format(message, args));

        if (t == null) {
            return;
        }

        log(tag, "exception.class=" + t.getClass().getName());

        if (t.getMessage() != null) {
            log(tag, "exception.message=" + t.getMessage());
        }

        StackTraceElement[] st = t.getStackTrace();
        if (st == null) {
            return;
        }

        int max = Math.min(6, st.length);
        for (int i = 0; i < max; i++) {
            log(tag, "at " + st[i]);
        }
    }

    public static void flushTo(Consumer<String> sink) {
        if (sink == null) {
            return;
        }

        String line;
        while ((line = QUEUE.poll()) != null) {
            sink.accept(line);
        }
    }

    private static String format(String msg, Object... args) {
        if (msg == null) {
            return "";
        }
        if (args == null || args.length == 0) {
            return msg;
        }
        return String.format(msg.replace("{}", "%s"), args);
    }

    private FragmentoLog() {}
}