package View_Controller;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * Main method.
 * Javadocs folder is located in the Joseph-Demuth-c482 folder.
 *
 * @author joseph
 */
public class Main extends Application {

    /**
     *
     * FUTURE ENHANCEMENT, One future addition I would like to add, would be buttons for the increment and decrement of the inventory of objects from the main screen.
     * This would make it easier to keep track of current inventory levels, by making it easy to update sales or procurement.
     * loads the main screen.
     * @param primaryStage
     *
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("Joseph Demuth C482");
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.show();
    }

    /**
     * Main method.
     * Populates the allParts and allProducts ObservablearrayList.
     * @param args
     *
     */
    public static void main(String[] args) {

        Inhouse inhouse1 = new Inhouse(1, "Harddrive", 65.00, 12, 5, 25, 25);
        Inhouse inhouse2 = new Inhouse(2, "Battery", 30.00, 8, 2, 10, 18);
        Inhouse inhouse3 = new Inhouse(3, "Power Converter", 5.00, 2, 10, 25, 6);



        Outsource outsource2 = new Outsource(4, "985 Processor", 112.00, 4, 2, 8,"Speedster" );
        Outsource outsource3 = new Outsource(5, "Touch screen", 49.99, 11, 4, 15,"Clarity" );
        Outsource outsource4 = new Outsource(6, "8 GB Ram", 23.49, 5, 5, 15,"Accumulation inc." );



        Product Product1 = new Product(1, "Laptop", 599.99, 7, 3, 8);

        Product1.addAPart(inhouse2);
        Product1.addAPart(inhouse3);
        Product1.addAPart(inhouse1);
        Product1.addAPart(outsource3);
        Product Product2 = new Product(2, "Desk Top", 749.99, 4, 2, 16);

        Product2.addAPart(inhouse2);
        Product2.addAPart(outsource2);
        Product2.addAPart(outsource4);
        Product Product3 = new Product(3, "Tablet", 399.99, 26, 10, 30);


        Product3.addAPart(inhouse2);
        Product3.addAPart(inhouse3);
        Product3.addAPart(inhouse1);
        Product3.addAPart(outsource3);



        Inventory.addPart(inhouse1);
        Inventory.addPart(inhouse2);
        Inventory.addPart(inhouse3);
        Inventory.addPart(outsource2);
        Inventory.addPart(outsource3);
        Inventory.addPart(outsource4);

        Inventory.addProduct(Product1);
        Inventory.addProduct(Product2);
        Inventory.addProduct(Product3);


        launch(args);
    }
}