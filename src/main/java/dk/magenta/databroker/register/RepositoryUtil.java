package dk.magenta.databroker.register;

import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.LatestCondition;
import dk.magenta.databroker.register.conditions.SingleCondition;

import java.util.regex.Pattern;

/**
 * Created by lars on 10-12-14.
 */
public abstract class RepositoryUtil {

    private static final Pattern onlyDigits = Pattern.compile("^\\*?\\-?\\d+\\*?$"); // Match a string containing only digits and optional leading and/or trailing wildcards
    private static final Pattern hasWildcard = Pattern.compile("^.*\\*.*$"); // Match a string that contains a wildcard
    private static final String surroundingWildcards = "^\\*+|\\*+$"; // For replacement; finds any series of leading or trailing wildcards
    private static final Pattern negated = Pattern.compile("^!.+$");
    private static final String leadingNegator = "^!"; // For replacement; finds any series of leading or trailing wildcards
    private static final String LIKE = "like";
    private static final String NOT_LIKE = "not like";
    private static final String EQUALS = "=";
    private static final String NOT_EQUALS = "!=";

    // Analyzes the search value and returns corresponding Condition
    // If the search parameter only contains digits and no wildcards, we want to search for that specific value (ie. WHERE [digitKey] = [search])
    // If the search parameter only contains digits and wildcards, we want to search for a partial match (e.g. WHERE cast([digitKey] as string) LIKE %[search]%)
    // If the search parameter contains letters and no wildcards, we want to search for that specific value (e.g. WHERE [nameKey] = [search])
    // If the search parameter contains letters and wildcards, we want to search for a partial match (e.g. WHERE [nameKey] = %[search]%)

    public static Condition whereField(int search, String digitKey, String nameKey) {
        return whereField((Object)search, digitKey, nameKey);
    }
    public static Condition whereField(long search, String digitKey, String nameKey) {
        return whereField((Object)search, digitKey, nameKey);
    }

    public static Condition whereField(String[] search, String digitKey, String nameKey) {
        ConditionList conditionList = new ConditionList(ConditionList.Operator.OR);
        for (String s : search) {
            conditionList.addCondition(RepositoryUtil.whereField(s, digitKey, nameKey));
        }
        return conditionList;
    }
    public static Condition whereField(String search, String digitKey, String nameKey) {
        return whereField((Object)search, digitKey, nameKey);
    }
    private static Condition whereField(Object search, String digitKey, String nameKey) {
        if (search == null) { // Should never happen
            throw new IllegalArgumentException("Parameter 'search' must not be null");
        }
        String strSearch = search.toString();
        boolean wildcardPresent = hasWildcard.matcher(strSearch).matches();
        boolean negate = negated.matcher(strSearch).matches();
        strSearch = strSearch.replaceAll(leadingNegator, "");
        boolean digits = onlyDigits.matcher(strSearch).matches();
        if (wildcardPresent) {
            strSearch = strSearch.replaceAll(surroundingWildcards, "%");
        }

        if (digitKey != null && digits) {
            if (wildcardPresent) {
                return new SingleCondition("cast(" + digitKey + " as string)", negate ? NOT_LIKE : LIKE, strSearch);
            } else {
                if (search instanceof String) {
                    search = Integer.parseInt(""+search, 10);
                }
                return new SingleCondition(digitKey, negate ? NOT_EQUALS : EQUALS, search);
            }
        } else {
            if (nameKey == null) {
                throw new IllegalArgumentException("Parameter 'nameKey' must not be null when 'digitKey' is null or search contains non-digits (search: "+search+", strSearch: "+strSearch+", digitKey: "+digitKey+", nameKey: "+nameKey+")");
            }
            if (wildcardPresent) {
                return new SingleCondition(nameKey, negate ? NOT_LIKE : LIKE, strSearch);
            } else {
                return new SingleCondition(nameKey, negate ? NOT_EQUALS : EQUALS, strSearch);
            }
        }
    }

    public static Condition whereFieldLand(String[] land) {
        return (land != null && land.length > 0) ? whereFieldLand(land[0]) : null;
    }
    public static Condition whereFieldLand(String land) {
        if (land != null) {
            land = land.toLowerCase();
            if (land.equals("gl") || land.equals("grønland")) {
                return whereField("9*", "kommune.kode", null);
            } else if (land.equals("dk") || land.equals("danmark")) {
                return whereField("!9*", "kommune.kode", null);
            }
        }
        return null;
    }

    public static Condition whereVersionLatest(String versionField) {
        return new LatestCondition(versionField);
    }

}
