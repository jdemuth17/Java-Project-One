/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * Controller for the Modify Product Scene.
 *
 * @author joseph
 */
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;
    String alertString = "Error message";

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @FXML
    private TextField IDTF;
    @FXML
    private TextField NameTF;
    @FXML
    private TextField InvTF;
    @FXML
    private TextField PriceTF;
    @FXML
    private TextField MaxTF;
    @FXML
    private TextField MinTF;
    @FXML
    private TextField ModifyProductSearch;
    @FXML
    private TableView<Part> AssociatedPartTable;
    @FXML
    private TableColumn<Part, Integer> RAPPartID;
    @FXML
    private TableColumn<Part, String> RAPPartName;
    @FXML
    private TableColumn<Part, Integer> RAPInventory;
    @FXML
    private TableColumn<Part, Double> RAPPrice;
    @FXML
    private TableView<Part> MPPartDataTable;
    @FXML
    private TableColumn<Part, Integer> MPDPartID;
    @FXML
    private TableColumn<Part, String> MPDPartName;
    @FXML
    private TableColumn<Part, Integer> MPDInventory;
    @FXML
    private TableColumn<Part, Double> MPDPrice;

    private ObservableList<Part> AddedPartsList = FXCollections.observableArrayList();

    Product sentProduct;
    public int index;

    /**
     * Searches all available parts. Dynamically updated search by name or partID.
     * @param keyEvent Updates search on key press.
     */
    public void APsearch(KeyEvent keyEvent) {
        FilteredList<Part> FoundParts = new FilteredList<>(FXCollections.observableArrayList(Inventory.getAllParts()));
        ModifyProductSearch.textProperty().addListener((observable, oldValue, newValue) -> {

            FoundParts.setPredicate(Part -> {
                if (newValue.isBlank())
                    return true;
                else if (Part.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;
                else if (String.valueOf(Part.getId()).contains(newValue))
                    return true;
                else
                    return false;
            });
            SortedList<Part> sortedParts = new SortedList<>(FoundParts);

            sortedParts.comparatorProperty().bind(MPPartDataTable.comparatorProperty());

            MPPartDataTable.setItems(sortedParts);
            MPPartDataTable.getSortOrder().setAll(MPDPartID);

        });
    }

    /**
     * Cancel button returns to main screen.
     * @param actionEvent returns to main screen and throws a dialog box for confirmation.
     */
    public void MPCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Confirm Cancel ");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                try {
                    scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(new Scene(scene));
                stage.show();
            }
        });
    }

    /**
     * gets the index of Product selected for modification.
     * @param index
     */
    public void getIndexx(int index){
        this.index = index;
        System.out.println(index);
    }

    /**
     * receives data for the Product to be modified .
     * @param SentProduct
     */
    public void receiveProduct(Product SentProduct) {

        IDTF.setText(String.valueOf(SentProduct.getID()));
        NameTF.setText(String.valueOf(SentProduct.getName()));
        InvTF.setText(String.valueOf(SentProduct.getStock()));
        PriceTF.setText(String.valueOf(SentProduct.getPrice()));
        MaxTF.setText(String.valueOf(SentProduct.getMax()));
        MinTF.setText(String.valueOf(SentProduct.getMin()));

        AddedPartsList.setAll(SentProduct.getAllAssociatedparts());

        AssociatedPartTable.setItems(AddedPartsList);
        RAPPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        RAPPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        RAPInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        RAPPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }


    /**
     * adds part to the a Products associated parts.
     * @param actionEvent adds parts from the parts table to hte associated Parts table.
     */
    public void AddPart(ActionEvent actionEvent) {

        Part addedPart = MPPartDataTable.getSelectionModel().getSelectedItem();

        if(addedPart != null) {
            AddedPartsList.add(addedPart);

            AssociatedPartTable.setItems(AddedPartsList);
            RAPPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
            RAPPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            RAPInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
            RAPPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        }else {
            alertString = "Select a part to add.";
            myAlert();
        }
    }

    /**
     * removes part to the a Products associated parts.
     * @param actionEvent removes parts from the parts table to the associated Parts table.
     */
    public void RemoveAssociatedPart(ActionEvent actionEvent) {

        Part addPart = AssociatedPartTable.getSelectionModel().getSelectedItem();

        if(addPart != null) {
            AddedPartsList.remove(addPart);
        }else{
            alertString = "Select a part to remove.";
            myAlert();
        }
    }

    /**
     * Adds a new part with the same id as product to be modified and deletes the part at that index.
     * @param actionEvent Saves a modified product.
     *
     */
    public void SaveProduct(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(IDTF.getText());
            String name = NameTF.getText();
            int stock = Integer.parseInt(InvTF.getText());
            Double price = Double.parseDouble(PriceTF.getText());
            int min = Integer.parseInt(MinTF.getText());
            int max = Integer.parseInt(MaxTF.getText());

            if (Validated(name, stock, min, max)) {

                Product addProduct = new Product(id, name, price, stock, min, max);

                Inventory.addProduct(addProduct);

                for(Part part : AddedPartsList){
                    addProduct.addAPart(part);
                }
                Inventory.DeleteModifiedProduct(index);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (Exception e) {
            alertString = "All fields required.";
            myAlert();
        }
    }

    /*
     * method that validates user input.
     */
    private boolean Validated (String name,int stock, int min, int max) {

        boolean valid = true;
        int count = 4;

        if (name.isEmpty() || name.matches("\\d+")) {
            alertString = "Name is required. Expects a String.";
            myAlert();
        } else count--;

        if(name != null){
            count--;
        }else {
            alertString = "Name is required. Expects a String.";
            myAlert();
        }

        if ((Integer.parseInt(MaxTF.getText())) > (Integer.parseInt(MinTF.getText()))) {
            count--;
        } else {
            alertString = "Max must be greater than min.";
            myAlert();
        }

        if ((Integer.parseInt(InvTF.getText())) <= (Integer.parseInt(MaxTF.getText())) && (Integer.parseInt(InvTF.getText())) >= (Integer.parseInt(MinTF.getText()))) {
            count--;
        } else {
            alertString = "Inv must be between min and max.";
            myAlert();
        }

        if (count > 0)
            valid = false;

        return valid;
    }

    // Validates user input.
    private boolean validate (String name) {
        boolean valid = true;
        if(name.isBlank() || name.matches("\\d+")) {
            alertString = "Name is required. Expects a String.";
            myAlert();
            valid = false;
        }
        return valid;
    }
    //Validates user input.
    private boolean vOutSource(String machineID){
        boolean OSV = true;
        String regex1 = "[A-Za-z\\s]+";
        if(machineID.isBlank()) {
            alertString = "Company Name is required.";
            myAlert();
            OSV = false;

        }
        return OSV;
    }

    //Validates user input.
    private boolean vInHouse(String machineID){
        boolean IHV = true;
        if ( machineID.matches("[A-Za-z\\s]+") || machineID.isBlank()){
            alertString = "machineID is required. Expects an Integer.";
            myAlert();
            IHV = false;
        }else if(machineID.matches("\\d+")){
            IHV = true;
        }
        return IHV;
    }

    //Error message to to be reused for different errors.
    private void myAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(alertString);
        alert.showAndWait();
    }

    /**
     *  Initializes the controller class.Implements change listeners for input fields and populates the tables.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        MPPartDataTable.setItems(Inventory.getAllParts());
        MPDPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MPDPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        MPDInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MPDPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        MinTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    MinTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        MaxTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    MaxTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        InvTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    InvTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        PriceTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*[.]\\d{1,2}")) {
                    PriceTF.setText(newValue.replaceAll("[^\\d*[.][0-9]{1,2}]", ""));
                }
            }
        });
    }
}
