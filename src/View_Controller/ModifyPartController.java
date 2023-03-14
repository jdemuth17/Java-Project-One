/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Model.Inhouse;
import Model.Inventory;
import Model.Outsource;
import Model.Part;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;



/**
 * Controller for the Modify Part Scene.
 *
 * @author joseph
 */
public class ModifyPartController implements Initializable {

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
    private TextField maxTF;
    @FXML
    private TextField minTF;
    @FXML
    private TextField variableTF;
    @FXML
    public Label VariableLabel;
    @FXML
    public RadioButton OutsourcedRB;
    @FXML
    public RadioButton InhouseRB;

    private Part SentPart;
    private int index;



    /**
     * Cancel button returns to main screen.
     * @param actionEvent returns to main screen and throws a dialog box for confirmation.
     */
    public void Cancelbutton(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Confirm Cancel");
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
     * Switches to inhouse part controller.
     * @param actionEvent switches to inhouse controller when radiobutton is selected
     *
     */
    public void ClickedinhouseRB(ActionEvent actionEvent) throws IOException {

        VariableLabel.setText("machineID");
        variableTF.setText(String.valueOf((0)));


    }
    /**
     * Switches to outsource part controller.
     * @param actionEvent switches to outsource controller when radiobutton is selected
     *
     */
    public void ClickedoutsourcedRB(ActionEvent actionEvent) throws IOException {

        VariableLabel.setText("Company Name");

    }

    /**
     * Receives Part object selected for modification on main screen part table
     * @param SentPart Part sent from main screen.
     */
    public void receivePart(Part SentPart){

        this.SentPart = SentPart;

        IDTF.setText(String.valueOf(SentPart.getId()));
        NameTF.setText(String.valueOf(SentPart.getName()));
        InvTF.setText(String.valueOf(SentPart.getStock()));
        PriceTF.setText(String.valueOf(SentPart.getPrice()));
        maxTF.setText(String.valueOf(SentPart.getMax()));
        minTF.setText(String.valueOf(SentPart.getMin()));

        if(SentPart instanceof Inhouse) {
            variableTF.setText(String.valueOf(((Inhouse) SentPart).getMachineID()));
        }else if(SentPart instanceof Outsource){
            variableTF.setText(String.valueOf(((Outsource) SentPart).getcompanyName()));
        }
    }

    /**
     * Gets index of selected Part to be modified from the main screen
     * @param index
     */
    public void getIndex(int index){
        this.index = index;
        System.out.println(index);
    }

    /**
     * Adds a new part with the same id as part to be modified and deletes the part at that index
     * @param actionEvent
     * @throws IOException
     */

    public void UpdatePart(ActionEvent actionEvent) throws IOException {

            if (InhouseRB.isSelected()) {
                try {
                    int id = Integer.parseInt(IDTF.getText());
                    String name = NameTF.getText();
                    int stock = Integer.parseInt(InvTF.getText());
                    Double price = Double.parseDouble(PriceTF.getText());
                    int min = Integer.parseInt(minTF.getText());
                    int max = Integer.parseInt(maxTF.getText());
                    String machineID = variableTF.getText();

                    if (Validated(name, stock, min, max) && vInHouse(machineID)) {

                        Inventory.addPart(new Inhouse(id, name, price, stock, min, max, Integer.parseInt(machineID)));
                        Inventory.DeleteModifiedPart(index);

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




        if( OutsourcedRB.isSelected()){
            try {
                int id = Integer.parseInt(IDTF.getText());
                String name = NameTF.getText();
                int stock = Integer.parseInt(InvTF.getText());
                Double price = Double.parseDouble(PriceTF.getText());
                int min = Integer.parseInt(minTF.getText());
                int max = Integer.parseInt(maxTF.getText());
                String machineID = variableTF.getText();

                if (Validated(name, stock, min, max) && vOutSource(machineID)) {

                    Inventory.addPart(new Outsource(id, name, price, stock, min, max, machineID));
                    Inventory.DeleteModifiedPart(index);

                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }catch (Exception e){
                alertString = "All fields required.";
                myAlert();
            }
        }

    }



    // Validates user input.
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

        if ((Integer.parseInt(maxTF.getText())) > (Integer.parseInt(minTF.getText()))) {
            count--;
        } else {
            alertString = "Max must be greater than min.";
            myAlert();
        }

        if ((Integer.parseInt(InvTF.getText())) <= (Integer.parseInt(maxTF.getText())) && (Integer.parseInt(InvTF.getText())) >= (Integer.parseInt(minTF.getText()))) {
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
     *  Initializes the controller class.Implements change listeners for input fields.
     *
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        minTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    minTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        maxTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxTF.setText(newValue.replaceAll("[^\\d]", ""));
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
