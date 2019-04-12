package ru.hse.app.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

class SortedProperties extends Properties {

    void store(BufferedWriter writer) throws IOException {
        synchronized (this) {
            List<String> keys = new ArrayList<>(super.stringPropertyNames());
            Collections.sort(keys);
            keys.forEach(key -> {
                try {
                    String val = super.getProperty(key);
                    writer.write(key + "=" + val);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
        }
    }

}
