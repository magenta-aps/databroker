package dk.magenta.databroker.util.objectcontainers;

import java.util.Date;

/**
* Created by lars on 16-12-14.
*/
public class ModelUpdateCounter {

    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private int chunkItemsProcessed = 0;
    private int inputChunks = 0;
    private int chunkSize = 1000;

    public ModelUpdateCounter(){
        this(DEFAULT_CHUNK_SIZE);
    }

    public ModelUpdateCounter(int chunkSize){
        if (chunkSize > 0) {
            this.chunkSize = chunkSize;
        }
    }

    public void printEntryProcessed() {
        this.chunkItemsProcessed++;
        if (this.chunkItemsProcessed >= this.chunkSize) {
            this.chunkItemsProcessed = this.chunkItemsProcessed - this.chunkSize;
            this.inputChunks++;
            this.print();
        }
    }

    private void print() {
        System.out.println("    " + (this.inputChunks * this.chunkSize + this.chunkItemsProcessed) + " entries processed");
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
