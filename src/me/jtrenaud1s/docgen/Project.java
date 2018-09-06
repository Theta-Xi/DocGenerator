package me.jtrenaud1s.docgen;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private File sourceDirectory;
    private List<File> outputImages;
    private File outputDir;
    private String fileName;

    public Project(String src, List<File> imgs, String outDir, String filename) {
        sourceDirectory = new File(src);
        outputImages = imgs;
        outputDir = new File(outDir);
        fileName = filename;
    }


    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public List<File> getOutputImages() {
        return outputImages;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void save() throws IOException, InvalidFormatException {
        System.out.println("Processing Java Source Files...");

        List<File> sourceFiles = getFilesInDir(getSourceDirectory());
        FileOutputStream out = new FileOutputStream(new File(getOutputDir(), getFileName()));
        CustomXWPFDocument document = new CustomXWPFDocument();

        int i = 0;
        for (File file : sourceFiles) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(file.getName() + ":");
            run.addBreak();
            run.setBold(true);
            document.setParagraph(paragraph, i);
            i++;

            JavaFile jf = new JavaFile(file);
            XWPFParagraph paragraph2 = document.createParagraph();
            XWPFRun run2 = paragraph.createRun();
            int j = 0;
            for(String line : jf.getLines()) {
                run2.setText(line, j);
                run2.addBreak();
                run2.setBold(false);
                j++;
            }

            document.setParagraph(paragraph2, i);
            i++;

        }

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setText("Output:");
        run.addCarriageReturn();
        document.setParagraph(paragraph, i);

        System.out.println("Processing Output Screenshots...");

        for (File f : outputImages) {
            BufferedImage bimg = ImageIO.read(f);
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            String blipId = document.addPictureData(new FileInputStream(f), Document.PICTURE_TYPE_PNG);
            document.createPicture(blipId, document.getNextPicNameNumber(Document.PICTURE_TYPE_PNG), width, height);
        }
        document.write(out);
        out.close();
    }


    private List<File> getFilesInDir(File dir) {
        List<File> sourceFiles = new ArrayList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".java")) {
                sourceFiles.add(f);
            }
            if (f.isDirectory())
                sourceFiles.addAll(getFilesInDir(f));
        }
        return sourceFiles;
    }

    private String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            String ls = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();

            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
