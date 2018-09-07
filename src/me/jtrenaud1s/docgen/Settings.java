package me.jtrenaud1s.docgen;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private File settingsFile;
    private Properties settings;
    private File homeDirectory;
    private File outputDirectory;

    public Settings() throws IOException {
        settingsFile = new File(System.getProperty("user.home"), "docgen.conf");
        settings = new Properties();
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
            setDefaults();
        }
        FileInputStream fin = new FileInputStream(settingsFile);
        settings.load(fin);
        homeDirectory = new File(settings.getProperty("homedir"));
        outputDirectory = new File(settings.getProperty("outputdir"));
    }

    private void setDefaults() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your projects directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        settings.setProperty("homedir", chooser.getSelectedFile().getAbsolutePath());

        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your desired Document Output Directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        settings.setProperty("outputdir", chooser.getSelectedFile().getAbsolutePath());

        settings.store(new FileOutputStream(settingsFile), "");
    }

    public File getHomeDirectory() {
        return homeDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}
