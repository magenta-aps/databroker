package dk.magenta.databroker.core;

/**
 * Created by lars on 27-01-15.
 */
public abstract class Util {

    public static boolean compare(String a, String b) {
        return a == null ? (b == null) : a.equals(b);
    }

    public static boolean compare(Object a, Object b) {
        return a == null ? (b == null) : a.equals(b);
    }


}
