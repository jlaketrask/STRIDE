package coreEngine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Shu Liu
 */
public class CSVWriter {

    private final FileWriter fw;
    private final BufferedWriter bw;
    private final String separation;

    public CSVWriter(FileWriter writer, char separation) {
        this.fw = writer;
        this.separation = Character.toString(separation);
        bw = new BufferedWriter(fw);
    }

    public void writeNext(String[] data) throws IOException {
        for (String str : data) {
            bw.write(str);
            bw.write(separation);
        }
        bw.newLine();
    }

    public void close() throws IOException {
        bw.close();
        fw.close();
    }
}
