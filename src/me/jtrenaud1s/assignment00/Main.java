package me.jtrenaud1s.assignment00;

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

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select the src folder in your project directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        srcDir = chooser.getSelectedFile().getAbsolutePath();

        outputFile = chooser.getSelectedFile().getParentFile().getName() + ".docx";

        JFileChooser chooser1 = new JFileChooser();
        chooser1.setDialogTitle("Select your output screenshot image(s): ");
        chooser1.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files", "png"));
        chooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser1.setMultiSelectionEnabled(true);
        chooser1.showDialog(null, "Select");

        for (File f : chooser1.getSelectedFiles()) {
            outputImgs.add(f);
        }

        JFileChooser chooser2 = new JFileChooser();
        chooser2.setDialogTitle("Select your output directory: ");
        chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser2.showDialog(null, "Select");

        outputDir = chooser2.getSelectedFile().getAbsolutePath();


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
