package store.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    public static List<String> load(final String path) throws IOException {
        List<String> rawProducts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(readFile(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                rawProducts.add(line);
            }
        }
        return rawProducts;
    }

    private static FileReader readFile(final String path) throws FileNotFoundException {
        return new FileReader(new File(path));
    }
}