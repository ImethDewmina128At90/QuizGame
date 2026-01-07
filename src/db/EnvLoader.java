package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static Map<String, String> envVariables = new HashMap<>();
    private static boolean loaded = false;

    public static void load() {
        if (loaded) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                // Split on first '=' only
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    envVariables.put(key, value);
                }
            }
            loaded = true;
            System.out.println("Environment variables loaded successfully!");
        } catch (IOException e) {
            System.err.println("Warning: Could not load .env file. Using default values.");
        }
    }

    public static String get(String key) {
        load();
        return envVariables.get(key);
    }

    public static String get(String key, String defaultValue) {
        load();
        String value = envVariables.get(key);
        return value != null ? value : defaultValue;
    }
}

