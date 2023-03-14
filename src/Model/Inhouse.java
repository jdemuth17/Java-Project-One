package Model;

/**
 *
 * @author Joseph DeMuth
 */

public class Inhouse extends Part {
   private int machineID;

    public Inhouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Returns the part machineID.
     * @return the machineID
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Sets the part machineID.
     * @param machineID sets the machineID
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
