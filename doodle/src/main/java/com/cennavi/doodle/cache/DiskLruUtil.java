package com.cennavi.doodle.cache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;


final class DiskLruUtil {
    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    private DiskLruUtil() {
    }

    static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];

            int count;
            while((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }

            String var4 = writer.toString();
            return var4;
        } finally {
            reader.close();
        }
    }

    static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        } else {
            File[] var2 = files;
            int var3 = files.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                File file = var2[var4];
                if (file.isDirectory()) {
                    deleteContents(file);
                }

                if (!file.delete()) {
                    throw new IOException("failed to delete file: " + file);
                }
            }

        }
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException var2) {
                throw var2;
            } catch (Exception var3) {
            }
        }

    }
}

