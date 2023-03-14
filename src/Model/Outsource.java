package Model;

/**
 *
 * @author Joseph DeMuth
 */

public class Outsource extends Part{
    private String companyName;

    public Outsource(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Returns the part company name.
     * @return the companyName
     */
    public String getcompanyName() {
        return companyName;
    }

    /**
     * Sets the part company name.
     * @param companyName sets the companyName
     */
    public void setcompanyName(String companyName) {
        this.companyName = companyName;
    }
}
