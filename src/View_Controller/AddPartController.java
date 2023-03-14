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
import com.sun.source.tree.TryTree;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the add Part Scene.
 *
 * @author joseph
 */
public class AddPartController implements Initializable {


    Stage stage;
    Parent scene;
    String alertString = "Error message";

    @FXML
    public TextField InvTF;
    @FXML
    public TextField PriceTF;
    @FXML
    public TextField maxTF;
    @FXML
    public TextField minTF;
    @FXML
    public RadioButton OutsourcedRB;
    @FXML
    public RadioButton InhouseRB;
    @FXML
    public Label VariableLable;
    @FXML
    private TextField IDTF;
    @FXML
    private TextField NameTF;
    @FXML
    private TextField variableTF;


    /**
     * Returns to main screen.
     *
     * @param actionEvent returns to main screen and throws a dialog box for confirmation.
     */
    public void onactionCanclebutton(ActionEvent actionEvent) {
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
     * Switches to inhouse part controller.
     *
     * @param actionEvent switches to inhouse controller when radiobutton is selected
     */
    public void ClickedinhouseRB(ActionEvent actionEvent) throws IOException {
        VariableLable.setText("machineID");
        variableTF.setText(String.valueOf((0)));
    }

    /**
     * Switches to outsource part controller.
     *
     * @param actionEvent switches to outsource controller when radiobutton is selected
     */
    public void ClickedoutsourcedRB(ActionEvent actionEvent) throws IOException {
        VariableLable.setText("Company Name");

    }

    /**
     * Saves new Inhouse Part and returns to main screen.
     *
     * @param actionEvent creates new inhouse part and validates input befor saving and returning to mainscreen.
     */
    public void APSavePart(ActionEvent actionEvent) throws IOException {

        if (InhouseRB.isSelected()) {
            try {
                int id = Model.Inventory.getNewPartId();
                String name = NameTF.getText();
                int stock = Integer.parseInt(InvTF.getText());
                Double price = Double.parseDouble(PriceTF.getText());
                int min = Integer.parseInt(minTF.getText());
                int max = Integer.parseInt(maxTF.getText());
                String machineID = variableTF.getText();

                if (Validated(name, stock, min, max) && vInHouse(machineID)) {

                    Inventory.addPart(new Outsource(id, name, price, stock, min, max, machineID));


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



        if(OutsourcedRB.isSelected())

    {
        try {
            int id = Model.Inventory.getNewPartId();
            String name = NameTF.getText();
            int stock = Integer.parseInt(InvTF.getText());
            Double price = Double.parseDouble(PriceTF.getText());
            int min = Integer.parseInt(minTF.getText());
            int max = Integer.parseInt(maxTF.getText());
            String machineID = variableTF.getText();

            if (Validated(name, stock, min, max) && vOutSource(machineID)) {

                Inventory.addPart(new Outsource(id, name, price, stock, min, max, machineID));


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

}



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


    //Error message to to be reused for different errors.
    private void myAlert(){
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
