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
    private File file;
    private File projectDirectory;
    private Main application;

    public Project(Main app, File project, File src, File outDir, File filename) {
        projectDirectory = project;
        sourceDirectory = src;
        outputImages = new ArrayList<>();
        outputDir = outDir;
        file = filename;
        application = app;
    }

    public File getProjectDirectory() {
        return projectDirectory;
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

    public File getFile() {
        return file;
    }

    public void save() throws IOException, InvalidFormatException {
        List<File> sourceFiles = getJavaFilesInDir(getSourceDirectory());
        application.log("Generating .docx for project " + projectDirectory.getName());
        application.log("Processing " + sourceFiles.size() + " Java Source Files...");

        outputImages = getImageFilesInDir(getProjectDirectory());
        File outputFile = getFile();
        FileOutputStream out = new FileOutputStream(outputFile);
        CustomXWPFDocument document = new CustomXWPFDocument();

        int i = 0;
        for (File file : sourceFiles) {
            String relative = sourceDirectory.toURI().relativize(file.toURI()).getPath();
            application.log("\t" + relative);
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
        if (outputImages.size() > 0) {
            application.log("Processing " + outputImages.size() + " Output Screenshots...");


            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setBold(true);
            run.setText("Output:");
            run.addCarriageReturn();
            document.setParagraph(paragraph, i);

            for (File f : outputImages) {
                String relative = projectDirectory.toURI().relativize(f.toURI()).getPath();
                application.log("\t" + relative);

                BufferedImage bimg = ImageIO.read(f);
                int width = bimg.getWidth();
                int height = bimg.getHeight();
                int pictureType = 0;
                if (isJPG(f))
                    pictureType = Document.PICTURE_TYPE_JPEG;
                else if (isPNG(f))
                    pictureType = Document.PICTURE_TYPE_PNG;
                String blipId = document.addPictureData(new FileInputStream(f), pictureType);
                document.createPicture(blipId, document.getNextPicNameNumber(pictureType), width, height);
            }
        } else {
            application.log("No output images to process...");
        }
        document.write(out);
        out.close();
        application.log("File saved to " + outputFile.getAbsolutePath());
        application.log("");
    }


    private List<File> getJavaFilesInDir(File dir) {
        List<File> sourceFiles = new ArrayList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".java")) {
                sourceFiles.add(f);
            }
            if (f.isDirectory())
                sourceFiles.addAll(getJavaFilesInDir(f));
        }
        return sourceFiles;
    }

    private List<File> getImageFilesInDir(File dir) {
        List<File> sourceFiles = new ArrayList<>();

        for (File f : dir.listFiles()) {
            if (isImage(f)) {
                sourceFiles.add(f);
            }
            if (f.isDirectory())
                sourceFiles.addAll(getImageFilesInDir(f));
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

    private boolean isImage(File f) {
        return f.getName().endsWith(".png") || f.getName().endsWith(".PNG") || f.getName().endsWith("jpg") || f.getName().endsWith("JPG") || f.getName().endsWith("jpeg") || f.getName().endsWith("JPEG");
    }

    private boolean isPNG(File f) {
        return f.getName().endsWith(".png") || f.getName().endsWith(".PNG");
    }

    private boolean isJPG(File f) {
        return f.getName().endsWith("jpg") || f.getName().endsWith("JPG") || f.getName().endsWith("jpeg") || f.getName().endsWith("JPEG");
    }
}
