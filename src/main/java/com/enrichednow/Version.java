package com.enrichednow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** The version of this SDK, as the build wrote it. */
final class Version {
    static final String VALUE = read();

    private Version() {
    }

    private static String read() {
        try (InputStream in = Version.class.getResourceAsStream("/com/enrichednow/version.properties")) {
            if (in != null) {
                Properties properties = new Properties();
                properties.load(in);
                String version = properties.getProperty("version");
                if (version != null && !version.isEmpty() && !version.startsWith("${")) {
                    return version;
                }
            }
        } catch (IOException ignored) {
            // falls through
        }
        return "unknown";
    }
}
