package com.pgalaxyp.fragmento.combat.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class BinaryIo {
    public static void writeUuid(DataOutput out, UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    public static UUID readUuid(DataInput in) throws IOException {
        return new UUID(in.readLong(), in.readLong());
    }

    public static void writeString(DataOutput out, String s) throws IOException {
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(b.length);
        out.write(b);
    }

    public static String readString(DataInput in) throws IOException {
        int len = in.readInt();
        if (len < 0 || len > 1_000_000) throw new IllegalArgumentException();
        byte[] b = new byte[len];
        in.readFully(b);
        return new String(b, StandardCharsets.UTF_8);
    }

    private BinaryIo() {}
}