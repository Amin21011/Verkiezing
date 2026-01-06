package Java;
import java.util.*;

public class MapTestDemo {
    private static final int RUNS = 1000;
    private static final int ITEMS = 10_000;
    private static final int LOOKUPS = 10_000;
    private static final int REMOVES = 100;

    public static void main(String[] args) {
        int[] keys = new int[ITEMS];
        String[] values = new String[ITEMS];
        int[] lookupIndices = new int[LOOKUPS];
        int[] removeIndices = new int[REMOVES];

        Random rand = new Random(12);
        for (int i = 0; i < ITEMS; i++) {
            keys[i] = rand.nextInt();
            values[i] = "Value" + i;
        }
        for (int i = 0; i < LOOKUPS; i++) {
            lookupIndices[i] = rand.nextInt(ITEMS);
        }
        for (int i = 0; i < REMOVES; i++) {
            removeIndices[i] = rand.nextInt(ITEMS);
        }
        benchmark("HashMap", HashMap::new, keys, values, lookupIndices, removeIndices);
        benchmark("LinkedHashMap", LinkedHashMap::new, keys, values, lookupIndices, removeIndices);
        benchmark("TreeMap", TreeMap::new, keys, values, lookupIndices, removeIndices);
    }

    private static void benchmark(
            String name,
            MapSupplier supplier,
            int[] keys,
            String[] values,
            int[] lookupIndices,
            int[] removeIndices
    ) {
        long totalInsert = 0;
        long totalLookup = 0;
        long totalRemove = 0;
        long totalMemory = 0;

        for (int r = 0; r < RUNS; r++) {
            System.gc();
            Map<Integer, String> demoMap = supplier.create();

            // INSERT
            long insertStart = System.nanoTime();
            for (int i = 0; i < ITEMS; i++) {
                demoMap.put(keys[i], values[i]);
            }
            long insertEnd = System.nanoTime();
            totalInsert += (insertEnd - insertStart);

            // MEMORY
            long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            totalMemory += usedMem;

            // LOOKUP
            long lookupStart = System.nanoTime();
            for (int index : lookupIndices) {
                demoMap.get(keys[index]);
            }
            long lookupEnd = System.nanoTime();
            totalLookup += (lookupEnd - lookupStart);

            // REMOVE
            long removeStart = System.nanoTime();
            for (int index : removeIndices) {
                demoMap.remove(keys[index]);
            }
            long removeEnd = System.nanoTime();
            totalRemove += (removeEnd - removeStart);
        }

        // microseconds
        long avgInsertUs = totalInsert / RUNS / 1_000;
        long avgLookupUs = totalLookup / RUNS / 1_000;
        long avgRemoveUs = totalRemove / RUNS / 1_000;

        double avgMemoryMb = (totalMemory / RUNS) / (1024.0 * 1024.0);

        System.out.printf(
                "%s → Insert: %d µs | Lookup: %d µs | Remove: %d µs | Memory: %.3f MB\n",
                name, avgInsertUs, avgLookupUs, avgRemoveUs, avgMemoryMb
        );
    }

    @FunctionalInterface
    interface MapSupplier {
        Map<Integer, String> create();
    }
}
