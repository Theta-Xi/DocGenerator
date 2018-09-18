package me.jtrenaud1s.docgen;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {

    private static String srcDir;
    private static String outputDir;
    private static String outputFile;
    private static String projectDir;
    private static Settings settings = null;
    private static JTextArea log;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            settings = new Settings();
        } catch (IOException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Docx Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 1;
        gbc.weightx = 1;
        JButton setting = new JButton("Change Settings");

        frame.add(setting, gbc);
        JButton generate = new JButton("Generate .docx");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = 1;
        gbc.weightx = 1;
        frame.add(generate, gbc);
        log = new JTextArea(15, 60);
        log.setAutoscrolls(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(new JScrollPane(log), gbc);
        log.setEditable(false);
        frame.pack();
        frame.setVisible(true);

        setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    settings.setDefaults();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDoc();
            }
        });

    }

    private static void createDoc() {
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(settings.getHomeDirectory());
        chooser.setDialogTitle("Select the your project's root directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() == null)
            return;
        projectDir = chooser.getSelectedFile().getAbsolutePath();
        srcDir = getSrcDir(projectDir);
        outputFile = chooser.getSelectedFile().getName() + ".docx";
        chooser.setCurrentDirectory(chooser.getSelectedFile());
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
                                            log("Project is NetBeans");
                                            return main.getAbsolutePath();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    log("Project is IntelliJ IDEA");
                    return f.getAbsolutePath();
                }
            }
        }
        return "";
    }

    public static void log(String log) {
        System.out.println(log);
        Main.log.setText(Main.log.getText() + log + "\n");
    }
}
