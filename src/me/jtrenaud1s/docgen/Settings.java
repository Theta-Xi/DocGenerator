package me.jtrenaud1s.docgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Main application;
    private JFileChooser chooser;
    private JFrame settingsFrame;

    public Settings(Main application) throws IOException {
        this.application = application;

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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

        settingsFrame = new JFrame("Settings");
        settingsFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JButton projectDir = new JButton("Project Root Directory");
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = 1;
        settingsFrame.add(projectDir, constraints);

        JButton outputDir = new JButton("docx Output Directory");
        constraints.gridx = 1;
        settingsFrame.add(outputDir, constraints);
        settingsFrame.pack();

        projectDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setProjectRoot();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        outputDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setOutputDir();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void setDefaults() throws IOException {
        application.log("Please set your directories in settings");
        JOptionPane.showMessageDialog(null, "Please set your directories in settings");
        setProjectRoot();
        setOutputDir();
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

    public void setProjectRoot() throws IOException {
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your projects directory: ");
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() != null) {
            settings.setProperty("homedir", chooser.getSelectedFile().getAbsolutePath());
            application.log("Projects Directory set to " + chooser.getSelectedFile().getAbsolutePath());
        }
        settings.store(new FileOutputStream(settingsFile), "");

    }

    public void setOutputDir() throws IOException {
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setDialogTitle("Select your desired Document Output Directory: ");
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() != null) {
            settings.setProperty("outputdir", chooser.getSelectedFile().getAbsolutePath());
            application.log(".docx Output Directory set to " + chooser.getSelectedFile().getAbsolutePath());
        }
        settings.store(new FileOutputStream(settingsFile), "");

    }

    public JFrame getSettingsFrame() {
        return settingsFrame;
    }

    public void showSettings() {
        settingsFrame.setVisible(true);
    }

    public void hideSettings() {
        settingsFrame.setVisible(false);
    }
}
