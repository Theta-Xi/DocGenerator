package me.jtrenaud1s.docgen;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String srcDir = "";
        String outputDir = "";
        String outputFile = "";
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
        chooser.setDialogTitle("Select the src folder in your project directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        srcDir = chooser.getSelectedFile().getAbsolutePath();

        outputFile = chooser.getSelectedFile().getParentFile().getName() + ".docx";

        chooser.setCurrentDirectory(chooser.getSelectedFile());

        chooser.setDialogTitle("Select your output screenshot image(s): ");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files", "png"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.showDialog(null, "Select");

        for (File f : chooser.getSelectedFiles()) {
            outputImgs.add(f);
        }

        outputDir = settings.getOutputDirectory().getAbsolutePath();


        Project project = new Project(srcDir, outputImgs, outputDir, outputFile);

        try {
            project.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
