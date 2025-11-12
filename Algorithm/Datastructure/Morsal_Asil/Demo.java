import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class DemoTest {
    public static void main(String args[]) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("One", "1");
        map.put("Five", "5");
        map.put("Four", "4");
        map.put("Two", "2");
        map.put("Three", "3");
        System.out.println("HashMap: \n" + map);

        Map<String, String> map1 = new LinkedHashMap<String, String>();
        map1.put("One", "1");
        map1.put("Five", "5");
        map1.put("Four", "4");
        map1.put("Two", "2");
        map1.put("Three", "3");
        System.out.println("LinkedHashMap: \n" + map1);

        Map<String, String> map2 = new TreeMap<String, String>();
        map2.put("One", "1");
        map2.put("Five", "5");
        map2.put("Four", "4");
        map2.put("Two", "2");
        map2.put("Three", "3");
        System.out.println("TreeMap: \n" + map2);
    }
}
