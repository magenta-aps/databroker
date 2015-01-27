package dk.magenta.databroker.util.objectcontainers;

/**
* Created by lars on 16-12-14.
*/
public class EntityModificationCounter {

    private int itemsCreated = 0;
    private int itemsUpdated = 0;

    public void countCreatedItem() {
        this.itemsCreated++;
    }

    public void countUpdatedItem() {
        this.itemsUpdated++;
    }

    public void printModifications() {
        if (this.itemsCreated > 0 || this.itemsUpdated > 0) {
            System.out.println("    " + this.itemsCreated + " new entries created\n    " + this.itemsUpdated + " existing entries updated");
        } else {
            System.out.println("    no changes necessary; old dataset matches new dataset");
        }
    }
}
