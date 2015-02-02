package dk.magenta.databroker.util;

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

}
