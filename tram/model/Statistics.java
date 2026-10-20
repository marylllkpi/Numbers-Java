package model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Counts allowed and denied passes. */
public class Statistics {
    private int allowed = 0;
    private int denied = 0;
    // ticket type -> {allowed, denied}
    private final Map<String, int[]> byType = new LinkedHashMap<>();

    public void record(String typeName, boolean isAllowed) {
        int[] counts = byType.computeIfAbsent(typeName, k -> new int[2]);
        if (isAllowed) {
            allowed++;
            counts[0]++;
        } else {
            denied++;
            counts[1]++;
        }
    }

    public int getAllowed() {
        return allowed;
    }

    public int getDenied() {
        return denied;
    }

    public Map<String, int[]> getByType() {
        return Collections.unmodifiableMap(byType);
    }
}
