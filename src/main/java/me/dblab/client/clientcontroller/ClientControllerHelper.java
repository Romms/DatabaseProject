package me.dblab.client.clientcontroller;

import javax.swing.*;

public class ClientControllerHelper {
    public static String queryPath() {
        return (String) JOptionPane.showInputDialog(
                null,
                "Enter Path:",
                "Path Request Dialog",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "sample.db"
        );
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
                "IntColumn,Int;CharColumn,Char;StringColumn,String"
        );
    }
}
