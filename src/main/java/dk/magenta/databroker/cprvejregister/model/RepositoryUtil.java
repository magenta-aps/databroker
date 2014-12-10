package dk.magenta.databroker.cprvejregister.model;

import java.security.InvalidParameterException;
import java.util.regex.Pattern;

/**
 * Created by lars on 10-12-14.
 */
public abstract class RepositoryUtil {

    private static final Pattern onlyDigits = Pattern.compile("^\\*?\\d+\\*?$"); // Match a string containing only digits and optional leading and/or trailing wildcards
    private static final Pattern hasWildcard = Pattern.compile("^.*\\*.*$"); // Match a string that has a leading or trailing wildcard
    private static final String surroundingWildcards = "^\\*+|\\*+$"; // For replacement; finds any series of leading or trailing wildcards
    private static final String LIKE = "like";
    private static final String EQUALS = "=";


    // Analyzes the search value and returns a set of data to be used in creating a query
    // For example:
    //  Object[] params = RepositoryUtil.whereField(kommune, "kommune.kommunekode", "kommune.latestVersion.navn");
    //  String where = "WHERE " + params[0] + " " + params[1] + " :kommune";
    //  parameters.put("kommune", params[2]);
    // If the search parameter only contains digits and no wildcards, we want to search for that specific value (ie. WHERE [digitKey] = [search])
    // If the search parameter only contains digits and wildcards, we want to search for a partial match (e.g. WHERE cast([digitKey] as string) LIKE %[search]%)
    // If the search parameter contains letters and no wildcards, we want to search for that specific value (e.g. WHERE [nameKey] = [search])
    // If the search parameter contains letters and wildcards, we want to search for a partial match (e.g. WHERE [nameKey] = %[search]%)

    public static Condition whereField(String search, String digitKey, String nameKey) {
        if (search == null) { // Should never happen
            throw new IllegalArgumentException("Parameter 'search' must not be null");
        }
        Object[] output = new Object[3]; // Will mostly contain strings, but occasionally an Integer
        boolean wildcardPresent = hasWildcard.matcher(search).matches();

        if (digitKey != null && onlyDigits.matcher(search).matches()) {
            if (wildcardPresent) {
                return new Condition("cast("+digitKey+" as string)", LIKE, search.replaceAll(surroundingWildcards,"%"));
            } else {
                return new Condition(digitKey, EQUALS, Integer.parseInt(search, 10));
            }
        } else {
            if (nameKey == null) {
                throw new IllegalArgumentException("Parameter 'nameKey' must not be null when 'digitKey' is null or search contains non-digits");
            }
            if (wildcardPresent) {
                return new Condition(nameKey, LIKE, search.replaceAll(surroundingWildcards,"%"));
            } else {
                return new Condition(nameKey, EQUALS, search);
            }
        }
    }

}
