package me.dblab.databasecontroller;

public class NullEscaper {
    private static final Character ESC = '0';

    public static String nullEscape(String s) {
        if (s == null) {
            return "";
        }

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != ESC) {
                return s;
            }
        }

        return s + ESC;
    }

    public static String nullUnescape(String s) {
        assert s != null;

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != ESC) {
                return s;
            }
        }

        if (s.length() == 0) {
            return null;
        }

        return s.substring(1);
    }
}
