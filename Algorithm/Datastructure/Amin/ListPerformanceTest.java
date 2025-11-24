import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListPerformanceTest {

    public static void main(String[] args) {
        int[] sizes = {100, 10_000, 100_000, 1_000_000};

        for (int size : sizes) {
            System.out.println("\n=== Testing size: " + size + " ===");
            testAddPerformance(size);
            testRemoveMiddleLinkedListAdvantage(size);
        }
    }

    private static void testAddPerformance(int size) {
        // ArrayList add
        List<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayList.add(i); // add at end
        }
        long end = System.nanoTime();
        System.out.println("ArrayList add end: " + (end - start) / 1_000_000.0 + " ms");

        // LinkedList add
        List<Integer> linkedList = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedList.add(i); // add at end
        }
        end = System.nanoTime();
        System.out.println("LinkedList add end: " + (end - start) / 1_000_000.0 + " ms");
    }

    private static void testRemoveMiddleLinkedListAdvantage(int size) {
        // ArrayList remove from middle using index
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) arrayList.add(i);
        long start = System.nanoTime();
        for (int i = 0; i < size / 10; i++) {
            arrayList.remove(arrayList.size() / 2);
        }
        long end = System.nanoTime();
        System.out.println("ArrayList remove middle: " + (end - start) / 1_000_000.0 + " ms");

        // LinkedList remove from middle using iterator (no repeated traversal)
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) linkedList.add(i);
        Iterator<Integer> iterator = linkedList.listIterator(size / 2); // start in the middle
        start = System.nanoTime();
        for (int i = 0; i < size / 10; i++) {
            iterator.next();
            iterator.remove(); // O(1) remove without traversing
        }
        end = System.nanoTime();
        System.out.println("LinkedList remove middle: " + (end - start) / 1_000_000.0 + " ms");
    }
}
