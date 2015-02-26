package dk.magenta.databroker.core;

import java.io.FilterInputStream;
import java.io.InputStream;

/**
* Created by lars on 26-02-15.
*/
public class NamedInputStream extends FilterInputStream {
    long knownSize = 0;
    String basename;
    String extension;

    public NamedInputStream(InputStream input, String filename) {
        super(input);
        this.basename = this.extractBasename(filename);
        this.extension = "";
    }
    public NamedInputStream(InputStream input, String basename, String extension) {
        super(input);
        this.basename = basename;
        this.extension = extension;
    }

    public String getBasename() {
        return basename;
    }
    public String getExtension() {
        return this.extension;
    }
    public boolean extensionEquals(String extension) {
        return this.extension != null && this.extension.equalsIgnoreCase(extension);
    }
    public long getKnownSize() {
        return knownSize;
    }
    public void setKnownSize(long knownSize) {
        this.knownSize = knownSize;
    }
    private String extractBasename(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? filename : filename.substring(0, index);
    }
    private String extractExtension(String filename) {
        int index = filename.lastIndexOf(".");
        return index == -1 ? "" : filename.substring(index);
    }
}
