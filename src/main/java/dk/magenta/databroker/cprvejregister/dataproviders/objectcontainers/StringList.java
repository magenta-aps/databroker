package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lars on 09-12-14.
 */
public class StringList extends ArrayList<String> {

    private String prefix = null;
    private String suffix = null;

    public StringList() {
    }

    public StringList(Collection<String> initial) {
        this.addAll(initial);
    }

    public void append(String s) {
        this.add(s);
    }
    public void append(List<String> s) {
        this.addAll(s);
    }
    public void append(String[] s) {
        for (int i=0; i<s.length; i++) {
            this.append(s[i]);
        }
    }

    public String join() {
        return this.join(null);
    }
    public String join(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> iter = this.iterator(); iter.hasNext(); ) {
            if (this.prefix != null) {
                sb.append(this.prefix);
            }
            sb.append(iter.next());
            if (this.suffix != null) {
                sb.append(this.suffix);
            }
            if (delimiter != null && iter.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
