package csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvReader implements Iterator<List<String>>{


    BufferedReader csvReader;
    String nextLine;

    char seprator = ',';
    char escape = '"';

    /**
     * Generates a new CsvReader which is also an Iterator
     * @param file the path to the file
     * @return CsvReader
     * @throws FileNotFoundException
     */
    public static Iterator<List<String>> fromFile(String file) throws FileNotFoundException {
        return new CsvReader(file);
    }

    private CsvReader(String file) throws FileNotFoundException {
        this.csvReader = new BufferedReader(new FileReader(file));
        this.forward();
    }

    void forward() {
        try {
            this.nextLine = this.csvReader.readLine();
        } catch (IOException e) {
            System.out.println("CsvReader exception: " + e.getLocalizedMessage());
        }
    }

    private static List<String> tokenizeString(String s, char sep, char escape) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean inEscape = false;
        for (char c : s.toCharArray()) {
            if (inEscape && c == escape) {
                inEscape = false;
                continue;
            } else if (c == escape) {
                inEscape = true;
                continue;
            } else if (c == sep) {
                tokens.add(sb.toString());
                sb.setLength(0);
                continue;
            }
            sb.append(c);
        }

        tokens.add(sb.toString());

        return tokens;
    }

    @Override
        public boolean hasNext() {
            return this.nextLine != null;
        }

        @Override
        public List<String> next() {
            String temp = this.nextLine;
            this.forward();
            return tokenizeString(temp, this.seprator, this.escape);
        }

}
