package me.jtrenaud1s.docgen;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaFile {

    private ArrayList<String> lines;
    private File file;

    public JavaFile(File f) {
        this.file = f;
        this.lines = new ArrayList<>();
        readFile();
    }

    public List<String> getLines()
    {
        return lines;
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                lines.add(trim(line));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String trim(String str) {
        return str.replaceAll("(\\r|\\n)", "");
    }
}
