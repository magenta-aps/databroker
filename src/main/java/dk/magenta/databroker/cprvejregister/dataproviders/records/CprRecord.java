package dk.magenta.databroker.cprvejregister.dataproviders.records;
import dk.magenta.databroker.register.records.Record;

import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * Created by lars on 04-11-14.
 */
public abstract class CprRecord extends Record {

    public static final String RECORDTYPE_START = "000";
    public static final String RECORDTYPE_SLUT = "999";
    private static Pattern leadingZero = Pattern.compile("^0+");

    private String line;

    public CprRecord(String line) throws ParseException {
        if (line == null) {
            throw new ParseException("Invalid NULL input.", 0);
        }
        this.resetProcessed();
        this.line = line;
        String type = substr(line, 1, 3);
        String thisType = this.getRecordType();
        if (!type.equals(thisType)) {
            throw new ParseException("Invalid recordtype "+type+" for class "+this.getClass().getName()+", was expecting the input to begin with "+thisType+". Input was "+line+".", 0);
        }
    }

    protected String substr(String line, int position, int length) {
        return line.substring(Math.min(position - 1, line.length()), Math.min(position + length - 1, line.length())).trim();
    }

    protected void obtain(String key, int position, int length) {
        this.put(key, leadingZero.matcher(this.substr(this.line, position, length)).replaceFirst(""));
    }

    protected void clean() {
        this.line = null;
    }

}