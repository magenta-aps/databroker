package dk.magenta.databroker.util.objectcontainers;

import org.apache.log4j.Logger;

import java.util.Date;

/**
* Created by lars on 16-12-14.
*/
public class ModelUpdateCounter {

    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private int chunkItemsProcessed = 0;
    private int inputChunks = 0;
    private int chunkSize = 1000;
    private Logger log = Logger.getLogger(ModelUpdateCounter.class);

    public ModelUpdateCounter(){
        this(DEFAULT_CHUNK_SIZE);
    }

    public ModelUpdateCounter(int chunkSize){
        if (chunkSize > 0) {
            this.chunkSize = chunkSize;
        }
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public void countEntryProcessed() {
        this.chunkItemsProcessed++;
        if (this.chunkItemsProcessed >= this.chunkSize) {
            this.chunkItemsProcessed = this.chunkItemsProcessed - this.chunkSize;
            this.inputChunks++;
            this.log();
        }
    }

    public int getCount() {
        return this.inputChunks * this.chunkSize + this.chunkItemsProcessed;
    }

    private void print() {
        System.out.println("    " + this.getCount() + " entries processed");
    }

    private void log() {
        this.log.trace("    " + this.getCount() + " entries processed");
    }

    public void printFinalEntriesProcessed() {
        this.print();
    }

    public void reset() {
        this.inputChunks = 0;
    }
    public void reset(int newChunkSize) {
        this.reset();
        this.chunkSize = newChunkSize;
    }
}
