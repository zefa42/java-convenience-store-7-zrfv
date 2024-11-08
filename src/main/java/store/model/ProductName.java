package store.model;

import java.util.HashSet;
import java.util.Set;

public class ProductName {
    private static Set<String> names = new HashSet<>();

    private ProductName() {
    }

    public static void add(String name) {
        names.add(name);
    }

    public static Set<String> getNames() {
        return names;
    }
}
