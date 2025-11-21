package Java;

import java.util.*;

public class MapSpeedTest {

    public static void main(String[] args) {
        long hashMapInsertTotal = 0;
        long linkedHashMapInsertTotal = 0;
        long treeMapInsertTotal = 0;

        long hashMapLookupTotal = 0;
        long linkedHashMapLookupTotal = 0;
        long treeMapLookupTotal = 0;

        int runs = 50;
        int itemsToAdd = 100_000;

        // Vooraf gegenereerde sleutels en waarden (om random overhead te vermijden tijdens meting)
        int[] keys = new int[itemsToAdd];
        String[] values = new String[itemsToAdd];
        Random rand = new Random(42); // vaste seed voor reproduceerbaarheid

        for (int i = 0; i < itemsToAdd; i++) {
            keys[i] = rand.nextInt();
            values[i] = "Value" + i;
        }

        for (int run = 0; run < runs; run++) {

            Map<Integer, String> hashMap = new HashMap<>();
            long startInsertHM = System.nanoTime();
            for (int i = 0; i < itemsToAdd; i++) {
                hashMap.put(keys[i], values[i]);
            }
            long endInsertHM = System.nanoTime();
            hashMapInsertTotal += (endInsertHM - startInsertHM);

            // Lookup: 10.000 willekeurige sleutels
            long startLookupHM = System.nanoTime();
            for (int i = 0; i < 10_000; i++) {
                int index = rand.nextInt(itemsToAdd);
                hashMap.get(keys[index]);
            }
            long endLookupHM = System.nanoTime();
            hashMapLookupTotal += (endLookupHM - startLookupHM);

            Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
            long startInsertLHM = System.nanoTime();
            for (int i = 0; i < itemsToAdd; i++) {
                linkedHashMap.put(keys[i], values[i]);
            }
            long endInsertLHM = System.nanoTime();
            linkedHashMapInsertTotal += (endInsertLHM - startInsertLHM);

            long startLookupLHM = System.nanoTime();
            for (int i = 0; i < 10_000; i++) {
                int index = rand.nextInt(itemsToAdd);
                linkedHashMap.get(keys[index]);
            }
            long endLookupLHM = System.nanoTime();
            linkedHashMapLookupTotal += (endLookupLHM - startLookupLHM);

            Map<Integer, String> treeMap = new TreeMap<>();
            long startInsertTM = System.nanoTime();
            for (int i = 0; i < itemsToAdd; i++) {
                treeMap.put(keys[i], values[i]);
            }
            long endInsertTM = System.nanoTime();
            treeMapInsertTotal += (endInsertTM - startInsertTM);

            long startLookupTM = System.nanoTime();
            for (int i = 0; i < 10_000; i++) {
                int index = rand.nextInt(itemsToAdd);
                treeMap.get(keys[index]);
            }
            long endLookupTM = System.nanoTime();
            treeMapLookupTotal += (endLookupTM - startLookupTM);
        }

        double avgInsertHM = (hashMapInsertTotal / runs) / 100_000.0;
        double avgInsertLHM = (linkedHashMapInsertTotal / runs) / 100_000.0;
        double avgInsertTM = (treeMapInsertTotal / runs) / 100_000.0;

        double avgLookupHM = (hashMapLookupTotal / runs) / 100_000.0;
        double avgLookupLHM = (linkedHashMapLookupTotal / runs) / 100_000.0;
        double avgLookupTM = (treeMapLookupTotal / runs) / 100_000.0;

        System.out.println("Average Results over " + runs + " runs (" + itemsToAdd + " entries)");
        System.out.printf("HashMap: Insert = %.2f ms, Lookup = %.2f ms%n", avgInsertHM, avgLookupHM);
        System.out.printf("LinkedHashMap: Insert = %.2f ms, Lookup = %.2f ms%n", avgInsertLHM, avgLookupLHM);
        System.out.printf("TreeMap: Insert = %.2f ms, Lookup = %.2f ms%n", avgInsertTM, avgLookupTM);
    }
}
