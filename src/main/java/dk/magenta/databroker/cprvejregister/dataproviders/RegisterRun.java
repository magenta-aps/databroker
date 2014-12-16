package dk.magenta.databroker.cprvejregister.dataproviders;

import dk.magenta.databroker.cprvejregister.dataproviders.records.Record;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lars on 10-11-14.
 */
public class RegisterRun extends ArrayList<Record> {

    private int inputsProcessed = 0;
    private int inputChunks = 0;
    private long startTime = 0;

    public void startInputProcessing() {
        this.startTime = new Date().getTime();
    }

    public void printInputProcessed() {
        this.inputsProcessed++;
        if (this.inputsProcessed >= 1000) {
            this.inputsProcessed = 0;
            this.inputChunks++;
            System.out.println("    " + (this.inputChunks * 1000) + " inputs processed");
        }
    }

    public void printFinalInputsProcessed() {
        String timeStr = this.startTime != 0 ?
                " in "+String.format("%.3f", 0.001 * (new Date().getTime() - this.startTime))+" seconds" :
                "";
        System.out.println("Processed " + (1000 * this.inputChunks + this.inputsProcessed) + " inputs" + timeStr + ".");
    }

}
