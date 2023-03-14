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
 * Controller for the add Product Scene.
 *
 * @author joseph
 */
public class AddProductController implements Initializable {


    Stage stage;
    Parent scene;
    String alertString = "Error message";

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
    private TextField AddProductSearch;
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
    private TableView<Part> AllPartDataTable;
    @FXML
    private TableColumn<Part, Integer> APDPartID;
    @FXML
    private TableColumn<Part, String> APDPartName;
    @FXML
    private TableColumn<Part, Integer> APDInventory;
    @FXML
    private TableColumn<Part, Double> APDPrice;

    private ObservableList<Part> AddedPartsList = FXCollections.observableArrayList();


    /**
     * Searches all available parts. Dynamically updated search by name or partID.
     * @param keyEvent Updates search on key press.
     */
    public void APsearch(KeyEvent keyEvent) {
        FilteredList<Part> FoundParts = new FilteredList<>(FXCollections.observableArrayList(Inventory.getAllParts()));
        AddProductSearch.textProperty().addListener((observable, oldValue, newValue) -> {

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

            sortedParts.comparatorProperty().bind(AllPartDataTable.comparatorProperty());

            AllPartDataTable.setItems(sortedParts);
            AllPartDataTable.getSortOrder().setAll(APDPartID);

        });
    }


    /**
     * Cancel button returns to main screen.
     * @param actionEvent returns to main screen and throws a dialog box for confirmation.
     */
    public void onactionAPCancelButton(ActionEvent actionEvent){
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
     * adds part to the a Products associated parts.
     * @param actionEvent adds parts from the parts table to hte associated Parts table.
     */
    public void APAdd(ActionEvent actionEvent) {


            Part addedPart = AllPartDataTable.getSelectionModel().getSelectedItem();

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
    @FXML
    private void RemoveAssociatedPart(ActionEvent actionEvent) {

        Part addPart = AssociatedPartTable.getSelectionModel().getSelectedItem();

        if(addPart != null) {
            AddedPartsList.remove(addPart);
        }else {
            alertString = "Select a part to remove.";
            myAlert();

        }

    }

    /**
     * removes part to the a Products associated parts.
     * @param actionEvent removes parts from the parts table to the associated Parts table.
     */
    @FXML
    private void APSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Model.Inventory.getNewPartId();
            String name = NameTF.getText();
            int stock = Integer.parseInt(InvTF.getText());
            Double price = Double.parseDouble(PriceTF.getText());
            int min = Integer.parseInt(MinTF.getText());
            int max = Integer.parseInt(MaxTF.getText());


            if (Validated(name, stock, min, max)) {

               Product addProduct = new Product(Model.Inventory.getNewProductId(), name, price, stock, min, max);

               for(Part part : AddedPartsList){
                   addProduct.addAPart(part);
               }

               Inventory.addProduct(addProduct);

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






    /**
     *
     * method that valiadates user input.
     * @return validates user input before it is saved, throws dialog boxes for errors.
     */
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


    /*
     * Error messages
     * different error messages to be displayed.
     */


    public void myAlert(){
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

        AllPartDataTable.setItems(Inventory.getAllParts());
        APDPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        APDPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        APDInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        APDPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

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
