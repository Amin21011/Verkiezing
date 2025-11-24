import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashSet;

public class SetDemoTest {

    public static void main(String[] args) {

        System.out.println("---- INSERTION TEST ----");
        long startHash = System.currentTimeMillis();
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < 100000; i++) hashSet.add(i);
        long endHash = System.currentTimeMillis();
        System.out.println("HashSet time: " + (endHash - startHash) + " ms");

        long startTree = System.currentTimeMillis();
        Set<Integer> treeSet = new TreeSet<>();
        for (int i = 0; i < 100000; i++) treeSet.add(i);
        long endTree = System.currentTimeMillis();
        System.out.println("TreeSet time: " + (endTree - startTree) + " ms");

        System.out.println("\n---- ORDER TEST ----");
        Set<String> hashNames = new HashSet<>();
        hashNames.add("banaan");
        hashNames.add("framboos");
        hashNames.add("druiven");
        System.out.println("HashSet order: " + hashNames); // HashSet: order is unpredictable

        Set<String> linkedHashNames = new LinkedHashSet<>();
        linkedHashNames.add("banaan");
        linkedHashNames.add("framboos");
        linkedHashNames.add("druiven");
        System.out.println("LinkedHashSet order: " + linkedHashNames); // LinkedHashSet: maintains insertion order

        Set<String> treeNames = new TreeSet<>();
        treeNames.add("banaan");
        treeNames.add("framboos");
        treeNames.add("druiven");
        System.out.println("TreeSet order: " + treeNames); // TreeSet: sorts elements naturally (alphabetically for Strings, numerically for Integers)

        System.out.println("\n---- DUPLICATE TEST ----");
        Set<String> animals = new HashSet<>();
        animals.add("Kip");
        animals.add("Kip");  // duplicate
        animals.add("Kuiken");
        System.out.println("Set contains: " + animals);
        System.out.println("Duplicate removed automatically");
    }
}
