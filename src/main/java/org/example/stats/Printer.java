package org.example.stats;

import java.util.List;
import java.util.Map;

public class Printer {

    public static void print() {
        Map<Integer, List<String>> stats = StatsUtils.getStats();
        System.out.println("=================");
        for (Map.Entry<Integer, List<String>> integerListEntry : stats.entrySet()) {
            Integer key = integerListEntry.getKey();
            List<String> data = integerListEntry.getValue();
            System.out.printf("Size of %s is: %s\n", key, data.size());
            System.out.printf("Data of %s is: %s\n", key, data);
        }
        System.out.println("Total size is: " + StatsUtils.getTotalSize());
        System.out.println("=================");
    }

}
