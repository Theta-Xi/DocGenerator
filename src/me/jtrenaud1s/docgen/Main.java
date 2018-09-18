package me.jtrenaud1s.docgen;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {

    private File srcDir;
    private File outputDir;
    private File outputFile;
    private File projectDir;
    private Settings settings;
    private JTextArea log;

    public Main() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            settings = new Settings(this);
        } catch (IOException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        outputDir = settings.getOutputDirectory();


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
        JButton open = new JButton("Open Output Folder");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = 1;
        gbc.weightx = 1;
        frame.add(open, gbc);
        log = new JTextArea(15, 60);
        log.setAutoscrolls(true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        frame.add(new JScrollPane(log), gbc);
        log.setEditable(false);
        frame.pack();
        frame.setVisible(true);

        setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.showSettings();
            }
        });

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createDoc();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!settings.hasValues())
                        settings.setDefaults();

                    Desktop.getDesktop().open(outputDir);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();

    }

    private void createDoc() throws IOException {

        if (!settings.hasValues())
            settings.setDefaults();
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(settings.getHomeDirectory());
        chooser.setDialogTitle("Select the your project's root directory: ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showDialog(null, "Select");
        if (chooser.getSelectedFile() == null)
            return;
        projectDir = chooser.getSelectedFile();
        srcDir = getSrcDir(projectDir);
        if (srcDir == null) {
            JOptionPane.showMessageDialog(chooser, "The selected folder is not a java project!");
            return;
        }
        outputFile = new File(outputDir, chooser.getSelectedFile().getName() + ".docx");

        Project project = new Project(this, projectDir, srcDir, outputDir, outputFile);

        try {
            project.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private File getSrcDir(File projectDir) {
        for (File f : projectDir.listFiles()) {
            if (f.isDirectory()) {
                if (f.getName().equalsIgnoreCase("src")) {
                    for (File f2 : f.listFiles()) {
                        if (f2.isDirectory()) {
                            if (f2.getName().equalsIgnoreCase("main")) {
                                for (File main : f2.listFiles()) {
                                    if (main.isDirectory()) {
                                        if (main.getName().equalsIgnoreCase("java")) {
                                            log("Project is NetBeans");
                                            return main;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    log("Project is IntelliJ IDEA");
                    return f;
                }
            }
        }
        return null;
    }

    public void log(String log) {
        System.out.println(log);
        this.log.setText(this.log.getText() + log + "\n");
    }
}
