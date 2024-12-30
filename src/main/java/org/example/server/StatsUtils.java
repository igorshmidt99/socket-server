package org.example.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StatsUtils {

    private static final Map<Integer, List<String>> stats = new ConcurrentHashMap<>();

    private static final AtomicInteger totalSize = new AtomicInteger(1);

    public static int getTotalSize() {
        return totalSize.get();
    }

    public static void initCache(int threadAmount) {
        if (!stats.containsKey(threadAmount)) {
            for (int i = 0; i < threadAmount; i++) {
                stats.put(i, new CopyOnWriteArrayList<>());
            }
        }
    }

    public static void add(int threadNumber, String data) {
        stats.get(threadNumber).add(data);
        totalSize.incrementAndGet();
    }

    public static void clear() {
        stats.clear();
        totalSize.set(1);
    }

    public static Map<Integer, List<String>> getStats() {
        return stats;
    }

    public static void print() {
        Printer.print();
    }
}
