package me.jtrenaud1s.docgen;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String srcDir = "";
        String outputDir = "";
        String outputFile = "";
        String projectDir = "";
        ArrayList<File> outputImgs = new ArrayList<>();
        Settings settings = null;
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            settings = new Settings();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(settings.getHomeDirectory());
        chooser.setDialogTitle("Select the your project's root directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        projectDir = chooser.getSelectedFile().getAbsolutePath();
        srcDir = getSrcDir(projectDir);

        outputFile = chooser.getSelectedFile().getName() + ".docx";

        chooser.setCurrentDirectory(chooser.getSelectedFile());

        for (File f : chooser.getSelectedFiles()) {
            outputImgs.add(f);
        }

        outputDir = settings.getOutputDirectory().getAbsolutePath();


        Project project = new Project(projectDir, srcDir, outputDir, outputFile);

        try {
            project.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private static String getSrcDir(String projectDir) {
        File project = new File(projectDir);
        for (File f : project.listFiles()) {
            if (f.isDirectory()) {
                if (f.getName().equalsIgnoreCase("src")) {
                    for (File f2 : f.listFiles()) {
                        if (f2.isDirectory()) {
                            if (f2.getName().equalsIgnoreCase("main")) {
                                for (File main : f2.listFiles()) {
                                    if (main.isDirectory()) {
                                        if (main.getName().equalsIgnoreCase("java")) {
                                            System.out.println("Project is NetBeans");
                                            return main.getAbsolutePath();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("Project is IntelliJ IDEA");
                    return f.getAbsolutePath();
                }
            }
        }
        return "";
    }
}
