package Model;
/**
 *
 * @author Joseph DeMuth
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    /**
     * RUNTIME ERROR I made was making the Prouduct attributes static. This caused all instances to be the same as the last product saved.
     */
    private int ID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();


    /**
     * RUNTIME ERROR I made was having Id instead of ID in product constructor which resulted in an id of 0 always.
     */

    public Product(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

    }

    /**
     * Returns the product ID.
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the product ID.
     * @param ID sets the ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     *Returns the product price.
     * @return the Price
     */
    public double getPrice() {
        return price;
    }

    /**
     *Sets the product price.
     * @param price sets the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *Returns the product name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     * @param name sets the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product stock.
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     *Sets the product stock.
     * @param stock sets the stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the product minimum stock
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the product minimum stock.
     * @param min sets the min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Returns the product maximum stock.
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the product maximum stock.
     * @param max sets the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds associated parts. Adds a part to the associatedParts list.
     */
    public void  addAPart(Part part) {
       associatedParts.add(part);
    }

    /**
     * Deletes an associated part. Deletes an associated part form the associatedParts list.
     * @param selectedAssociatedPart Deletes the associated part
     *
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
              return true;
        } else
            return false;
    }

    /**
     * Returns all associateParts. Returns all parts associated with a product.
     * @return Gets all associateParts list.
     */
    public ObservableList<Part> getAllAssociatedparts() {

        return associatedParts;
   }


}
