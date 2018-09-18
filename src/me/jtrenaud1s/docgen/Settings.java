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

    public void setDefaults() throws IOException {
        Main.log("Please set your directories in settings");
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your projects directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() != null) {
            settings.setProperty("homedir", chooser.getSelectedFile().getAbsolutePath());
            Main.log("Projects Directory set to " + chooser.getSelectedFile().getAbsolutePath());
        }
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your desired Document Output Directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() != null) {
            settings.setProperty("outputdir", chooser.getSelectedFile().getAbsolutePath());
            Main.log("Docx Output Directory set to " + chooser.getSelectedFile().getAbsolutePath());
        }
        settings.store(new FileOutputStream(settingsFile), "");
    }

    public File getHomeDirectory() {
        return homeDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public boolean hasValues() {
        return settings.containsKey("homedir") && settings.containsKey("outputdir");
    }
}
