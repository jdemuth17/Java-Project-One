package Model;

/**
 *
 * @author Joseph DeMuth
 */

import View_Controller.ModifyPartController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Adds a part to the allParts list.
     * @param part adds part to allParts observableArrayList.
     */
    public static void addPart(Part part){

        allParts.add(part);

    }

    /**
     * Returns all objects in the allParts List.
     * @return all objects in the allParts observableArrayList.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Adds Products to the allProducts observableArrayList.
     * @param product Adds a new product.
     */
    public static void addProduct(Product product){

        allProducts.add(product);
    }

    /**
     * Populates the getAllProducts observableArrayList from allProducts observableArrayList.
     * @return the allProducts observableArrayList.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;

    }

    /**
     * Removes Products from allProducts observableArrayList.
     * @param index Removes a product.
     */
    public static void DeleteModifiedProduct(int index) {
        allProducts.remove(index);
    }

    /**
     * Removes Parts form allParts observableArrayList.
     * @param index removes a Part.
     */
    public static void DeleteModifiedPart(int index) {
            allParts.remove(index);
    }

    /**
     * Removes Parts form allParts observableArrayList.
     * @param Part removes a Part.
     */
    public static void DeletePart(Part Part) {
        allParts.remove(Part);
    }

    /**
     * Removes Products from allProducts observableArrayList.
     * @param product removes a Product.
     *
     */
    public static boolean DeleteProduct(Product product){
        allProducts.remove(product);
        return true;
    }

    /**
     *Creates a new part id. Creates the first unused part id.
     * @return creates new Part id.
     */
    public static int getNewPartId() {

        int partId = 1;
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part AP : allParts)
            if (AP.getId() == partId){
                ++partId;
            }
        return partId;
    }


    /**
     * Creates a new product id. Creates the first unused product id.
     * @return creates new Products id.
     */
    public static int getNewProductId() {
        int productId = 1;
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product AP : allProducts)
            if (AP.getID() == productId){
                ++productId;
            }
        return productId;
    }


    /**
     * Lookup Part id. Looks up part by its id number.
     * @param PartID
     */
    public static void lookupPart(int PartID){
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part AP : allParts)
            if (AP.getId() == PartID){
                foundParts.add(AP);
            }

    }

    /**
     *
     * lookup Product ID.Looks up product by its id number.
     * @param ProductID
     */
    public static void lookupProduct(int ProductID){
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product APp : allProducts)
            if( APp.getID() == ProductID){
                foundProducts.add(APp);
            }
    }

    /**
     * Lookup Part name. Looks up part by its name.
     * @param PartName
     */

    public static void lookupPart(String PartName){
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Part AP : allParts)
            if (AP.getName().contains(PartName)){
                foundParts.add(AP);
            }

        }

    /**
     * lookup Product name. Looks up product by its name.
     * @param ProductName
     */
    public static void lookupProduct(String ProductName){

        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (Product APp : allProducts)
            if (APp.getName().contains(ProductName)){
                foundProducts.add(APp);
            }
    }

    /**
     * Modifies existing parts. Overwrites data of existing part.
     * @param index Index of part to be changed.
     * @param SentPart Part object to replace existing part.
     */
    public static void ModifyPart(int index,Part SentPart){

        allParts.set(index, SentPart);

    }


    /**
     * Modifies existing products. Overwrites data of existing product.
     * @param index Index of product to be changed.
     * @param SentProduct Product object to replace existing product.
     */
    public static void ModifyProduct(int index,Product SentProduct){

        allProducts.set(index, SentProduct);

    }



}
