package dk.magenta.databroker.cprvejregister.model;

import dk.magenta.databroker.register.conditions.Condition;
import dk.magenta.databroker.register.conditions.ConditionList;
import dk.magenta.databroker.register.conditions.SingleCondition;

import java.util.regex.Pattern;

/**
 * Created by lars on 10-12-14.
 */
public abstract class RepositoryUtil {

    private static final Pattern onlyDigits = Pattern.compile("^\\*?\\d+\\*?$"); // Match a string containing only digits and optional leading and/or trailing wildcards
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

    public static Condition whereField(String[] search, String digitKey, String nameKey) {
        ConditionList conditionList = new ConditionList(ConditionList.Operator.OR);
        for (String s : search) {
            conditionList.addCondition(RepositoryUtil.whereField(s, digitKey, nameKey));
        }
        return conditionList;
    }
    public static Condition whereField(String search, String digitKey, String nameKey) {
            if (search == null) { // Should never happen
            throw new IllegalArgumentException("Parameter 'search' must not be null");
        }
        boolean wildcardPresent = hasWildcard.matcher(search).matches();
        boolean negate = negated.matcher(search).matches();
        search = search.replaceAll(leadingNegator, "");
        boolean digits = onlyDigits.matcher(search).matches();
        if (wildcardPresent) {
            search = search.replaceAll(surroundingWildcards, "%");
        }

        if (digitKey != null && digits) {
            if (wildcardPresent) {
                return new SingleCondition("cast(" + digitKey + " as string)", negate ? NOT_LIKE : LIKE, search);
            } else {
                return new SingleCondition(digitKey, negate ? NOT_EQUALS : EQUALS, Integer.parseInt(search, 10));
            }
        } else {
            if (nameKey == null) {
                throw new IllegalArgumentException("Parameter 'nameKey' must not be null when 'digitKey' is null or search contains non-digits");
            }
            if (wildcardPresent) {
                return new SingleCondition(nameKey, negate ? NOT_LIKE : LIKE, search);
            } else {
                return new SingleCondition(nameKey, negate ? NOT_EQUALS : EQUALS, search);
            }
        }
    }

    public static Condition whereFieldLand(String land) {
        if (land != null) {
            land = land.toLowerCase();
            if (land.equals("gl") || land.equals("grønland")) {
                return whereField("9*", "kommune.kommunekode", null);
            } else if (land.equals("dk") || land.equals("danmark")) {
                return whereField("!9*", "kommune.kommunekode", null);
            }
        }
        return null;
    }

}
