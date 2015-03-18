package dk.magenta.databroker.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.Normalizer;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by lars on 27-01-15.
 */
public abstract class Util {

    public static boolean compare(int a, int b) {
        boolean answer = a == b;
        //if (!answer) System.out.println("int mismatch on "+a+" vs "+b);
        return answer;
    }

    public static boolean compare(String a, String b) {
        boolean answer = a == null ? (b == null) : a.equals(b);
        //if (!answer) System.out.println("string mismatch on "+a+" vs "+b);
        return answer;
    }

    public static boolean compare(Object a, Object b) {
        boolean answer = a == null ? (b == null) : a.equals(b);
        //if (!answer) System.out.println("object mismatch on "+a+" ("+(a!=null ? a.getClass().getSimpleName() : null)+") vs "+b+" ("+(b!=null ? b.getClass().getSimpleName() : null)+")");
        return answer;
    }

    public static boolean compare(Map a, Map b) {
        if (a == null || b == null) {
            boolean answer = a == b;
            //if (!answer) System.out.println("map mismatch on "+a+" vs "+b);
            return answer;
        } else if (a.size() != b.size()) {
            //System.out.println("map mismatch on "+a+" vs "+b);
            return false;
        } else {
            boolean answer = true;
            for (Object key : a.keySet()) {
                if (!b.containsKey(key) || !Util.compare(a.get(key), b.get(key))) {
                    answer = false;
                    break;
                }
            }
            //if (!answer) System.out.println("map mismatch on "+a+" vs "+b);
            return answer;
        }
    }

    public static boolean compare(Collection a, Collection b) {
        if (a == null || b == null) {
            //System.out.println("collection mismatch on "+a+" vs "+b);
            return a == b;
        } else if (a.size() != b.size()) {
            //System.out.println("collection mismatch on "+a+" vs "+b);
            return false;
        } else {
            boolean answer = a.containsAll(b) && b.containsAll(a);
            //if (!answer) System.out.println("collection mismatch on "+a+" vs "+b);
            return answer;
        }
    }

    public static boolean compare(Date a, Date b) {
        boolean answer = a == null ? (b == null) : a.compareTo(b)==0;
        //if (!answer) System.out.println("date mismatch on "+a.getTime()+" vs "+b.getTime());
        return answer;
    }

    public static boolean compare(char a, char b) {
        boolean answer = a == b;
        //if (!answer) System.out.println("char mismatch on "+a+" vs "+b);
        return answer;
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

    public static double getTime() {
        return 0.000001 * (double)System.nanoTime();
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
