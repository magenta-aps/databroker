package dk.magenta.databroker.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.Normalizer;
import java.util.Date;

/**
 * Created by lars on 27-01-15.
 */
public abstract class Util {

    public static boolean compare(int a, int b) {
        return a == b;
    }

    public static boolean compare(String a, String b) {
        return a == null ? (b == null) : a.equals(b);
    }

    public static boolean compare(Object a, Object b) {
        return a == null ? (b == null) : a.equals(b);
    }

    public static boolean compare(Date a, Date b) {
        return a == null ? (b == null) : a.equals(b);
    }

    public static boolean inArray(String[] array, String item) {
        if (array != null && item != null) {
            for (String i : array) {
                if (item.equals(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String emptyIfNull(String str) {
        return str == null ? "" : str;
    }

    public static String normalizeString(String str) {
        if (str != null && !str.isEmpty()) {
            return Normalizer.normalize(str.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}|[\\.']|\\s", "").trim();
        }
        return str;
    }
    public static boolean compareNormalized(String str1, String str2) {
        return compare(Util.normalizeString(str1), Util.normalizeString(str2));
    }

    public static long getTime() {
        return new Date().getTime();
    }

    public static long stringHash(String str) {
        long hash = 0;
        if (str != null) {
            for (int i=0; i<str.length(); i++) {
                hash = (hash << 8) + str.codePointAt(i);
            }
        }
        return hash;
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        StringWriter sw = new StringWriter();
        IOUtils.copy(inputStream, sw, "UTF-8");
        return sw.toString();
    }
}
