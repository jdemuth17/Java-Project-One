/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * Controller for the Main Screen
 *
 * @author Joseph DeMuth
 *
 */
public class Main_ScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField PartSearch;

    @FXML
    private TableView<Part> PartTable;

    @FXML
    private TableColumn<Part, Integer> PartIDCol;

    @FXML
    private TableColumn<Part, String> PartNameCol;

    @FXML
    private TableColumn<Part, Integer> PartInventoryLevelCol;

    @FXML
    private TableColumn<Part, Double> PartPriceCol;

    @FXML
    private TextField ProductSearch;

    @FXML
    private TableView<Product> ProductTable;

    @FXML
    private TableColumn<Product, Integer> ProductIDCol;

    @FXML
    private TableColumn<Product, String> ProductNameCol;

    @FXML
    private TableColumn<Product, Integer> ProductInventoryCol;

    @FXML
    private TableColumn<Product, Double> ProductPriceCol;


    /**
     * Initializes Mainscreen  controller.
     *Populates the main screen Parts and Products tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Populate main screen Part table

        PartTable.setItems(Inventory.getAllParts());
        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        //Populate main screen Product table

        ProductTable.setItems(Inventory.getAllProducts());
        ProductIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Searches all available parts. Dynamically updated search by name or partID.
     * @param keyEvent Updates search on key press.
     */
    @FXML
    private void updateSearch(KeyEvent keyEvent) {
        FilteredList<Part> FoundParts = new FilteredList<>(FXCollections.observableArrayList(Inventory.getAllParts()));

        PartSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FoundParts.setPredicate(Part -> {
                if(newValue.isBlank())
                    return true;
                else if(Part.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if(String.valueOf(Part.getId()).contains(newValue))
                    return true;
                else
                    return false;
            });
            SortedList<Part> sortedParts = new SortedList<>(FoundParts);

            sortedParts.comparatorProperty().bind(PartTable.comparatorProperty());

            PartTable.setItems(sortedParts);
            PartTable.getSortOrder().setAll(PartIDCol);

        });

    }


    /**
     * Searches all available products. Dynamically updated search by name or partID.
     * @param keyEvent Updates search on key press.
     */
    public void ProductSearch(KeyEvent keyEvent) {
        FilteredList<Product> FoundProducts = new FilteredList<>(FXCollections.observableArrayList(Inventory.getAllProducts()),b -> true);

        ProductSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FoundProducts.setPredicate(Product -> {
                        if(newValue.isBlank())
                            return true;
                        else if(Product.getName().toLowerCase().contains(newValue.toLowerCase()))
                            return true;
                        else if(String.valueOf(Product.getID()).contains(newValue))
                            return true;
                        else
                            return false;
                    });
            SortedList<Product> sortedProducts = new SortedList<>(FoundProducts);

            sortedProducts.comparatorProperty().bind(ProductTable.comparatorProperty());

            ProductTable.setItems(sortedProducts);
            ProductTable.getSortOrder().setAll(ProductIDCol);

                });
    }

    /**
     * Exit button closes the program.
     * @param actionEvent Exits the program after throwing a dialog box for confirmation.
     */
    public void onactionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Close the program?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    /**
     * Loads the AddInHousePart Scene.
     * @param actionEvent
     */
    public void onactionAddPart(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Add_InHouse_Part.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Sends Selected Part to modify Part screens.
     *
     * @param actionEvent Sends Selected Part from Parts table on main screen to text fields into both inHouse and outSource modify parts windows.
     */
    public void onactionModifyPart(ActionEvent actionEvent) throws IOException {

        Part selectedPart = PartTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {

            if (selectedPart instanceof Inhouse) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Modify_InHouse_Part.fxml"));
                loader.load();

                ModifyPartController MIHPController = loader.getController();
                MIHPController.receivePart(PartTable.getSelectionModel().getSelectedItem());
                MIHPController.getIndex(PartTable.getSelectionModel().getSelectedIndex());

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            } else if (selectedPart instanceof Outsource) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Modify_OutSource_Part.fxml"));
                loader.load();

                ModifyPartController MOSPController = loader.getController();
                MOSPController.receivePart(PartTable.getSelectionModel().getSelectedItem());
                MOSPController.getIndex(PartTable.getSelectionModel().getSelectedIndex());

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } else Alerts(8);
    }

    /**
     * Deletes selected Part. Deletes selected Part from main screen Parts table and throws a dialog box for confirmation.
     * @param actionEvent Deletes part when delete button is clicked.
     */

        public void onactionDeletePart(ActionEvent actionEvent) {
            Part selectedPart = PartTable.getSelectionModel().getSelectedItem();
            if(selectedPart != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Confirm delete " + selectedPart.getName());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Inventory.DeletePart(selectedPart);
                    }

                    PartTable.setItems(Inventory.getAllParts());
                    PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
                    PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                    PartInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
                    PartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

                });
            }else Alerts(7);
        }

    /**
     * Loads the Add Product Scene.
     * @param actionEvent
     */
    public void onactionAddProduct(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("Add_Product.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Loads the Modify Product Scene.
     * @param actionEvent
     */
    public void onactionModifyProduct(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = ProductTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View_Controller/Modify_Product.fxml"));
            loader.load();

            ModifyProductController MPPController = loader.getController();
            MPPController.receiveProduct(selectedProduct);
            MPPController.getIndexx(ProductTable.getSelectionModel().getSelectedIndex());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }else Alerts(8);
    }

    /**
     * Deletes selected Products. Deletes selected Products from main screen Product table and throws dialog box for confirmation.
     * @param actionEvent Deletes selected Product when button is clicked.
     */
    public void onactionDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = ProductTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            ObservableList<Part> AddedPartsList = selectedProduct.getAllAssociatedparts();
            if(AddedPartsList.size() == 0) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Product");
                alert.setContentText("delete " + selectedProduct.getName());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Inventory.DeleteProduct(selectedProduct);
                    }

                    ProductTable.setItems(Inventory.getAllProducts());
                    ProductIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
                    ProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                    ProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
                    ProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

                });
            }else Alerts(9);
        }else Alerts(7);
    }

    /**
     * Error messages
     * @param alerts different error messages to be called.
     */
    private void Alerts(int alerts) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alerts) {
            case 1:
                alert.setTitle("Error");
                alert.setContentText("All fields are required");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setContentText("Expects Numerical input");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setContentText("Min value must be between 0 and Max value");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setContentText("Inv value must be between Min and Max values");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setContentText("Expects String value");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Null Error");
                alert.setContentText("Select an item to Add.");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Null Error");
                alert.setContentText("Select an item to remove.");
                alert.showAndWait();
                break;
            case 8:
                alert.setTitle("Null Error");
                alert.setContentText("Select an item to modify.");
                alert.showAndWait();
                break;
            case 9:
                alert.setTitle("Warning.");
                alert.setContentText("Product has Associated Parts.");
                alert.showAndWait();
                break;
        }
    }
}