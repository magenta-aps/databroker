package dk.magenta.databroker.cprvejregister.dataproviders.objectcontainers;

import java.util.Date;

/**
* Created by lars on 16-12-14.
*/
public class InputProcessingCounter {

    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private int chunkInputsProcessed = 0;
    private int inputChunks = 0;
    private long startTime = 0;
    private int chunkSize = 1000;

    public InputProcessingCounter(){

    }
    public InputProcessingCounter(int chunkSize){
        this(chunkSize, false);
    }
    public InputProcessingCounter(boolean startNow){
        this(DEFAULT_CHUNK_SIZE, startNow);
    }


    public InputProcessingCounter(int chunkSize, boolean startNow){
        if (chunkSize > 0) {
            this.chunkSize = chunkSize;
        }
        if (startNow) {
            this.startInputProcessing();
        }
    }

    public void startInputProcessing() {
        this.startTime = new Date().getTime();
    }

    public void printInputProcessed() {
        this.chunkInputsProcessed++;
        if (this.chunkInputsProcessed >= this.chunkSize) {
            this.chunkInputsProcessed = 0;
            this.inputChunks++;
            System.out.println("    " + (this.inputChunks * this.chunkSize) + " inputs processed");
        }
    }

    public void printFinalInputsProcessed() {
        String timeStr = this.startTime != 0 ?
                " in "+String.format("%.3f", 0.001 * (new Date().getTime() - this.startTime))+" seconds" :
                "";
        System.out.println("Processed " + (this.inputChunks * this.chunkSize + this.chunkInputsProcessed) + " inputs" + timeStr + ".");
    }
}
