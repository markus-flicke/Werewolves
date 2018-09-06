package csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {

    String filePath;
    BufferedWriter csvWriter;
    char escape = '\"';
    char separator = ',';

    public CsvWriter(String filePath, boolean append) throws IOException {
        this.filePath = filePath;
        this.csvWriter = new BufferedWriter(new FileWriter(this.filePath, append));
    }

    public void writeLine(String... line) {
        try {
            for (int i = 0; i < line.length; i++) {
                this.csvWriter.write(this.escape + line[i] + this.escape);
                if (i < line.length-1) {
                    this.csvWriter.write(this.separator);
                }
            }
            csvWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            this.csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
