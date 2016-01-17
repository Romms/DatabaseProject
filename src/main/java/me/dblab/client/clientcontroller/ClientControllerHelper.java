package me.dblab.client.clientcontroller;

import javax.swing.*;

import javax.swing.JFileChooser;
import java.io.File;

public class ClientControllerHelper {
    public static String queryLoadPath() {
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parentFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        return null;
    }

    public static String querySavePath() {
        JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(parentFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        return null;
    }

    public static String queryTableName() {
        return (String) JOptionPane.showInputDialog(
                null,
                "Enter Table Name:",
                "Table Name Request Dialog",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "Table1"
        );
    }

    public static String queryTableScheme() {
        return (String) JOptionPane.showInputDialog(
                null,
                "Enter scheme in a following way.\n" +
                        "Each column is described by comma-separated name and type.\n" +
                        "Concatenate column descriptions with semicolon to a single string.\n" +
                        "Supported types are: Int, Double, Char, HTML, String, Long.",
                "Scheme request dialog",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "IntColumn,Int;CharColumn,Char;StringColumn,String;DateColumn,Date;DateIntervalColumn,DateInterval"
        );
    }
}
