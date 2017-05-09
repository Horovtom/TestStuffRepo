package com.cvut.fel.horovtom.presentation;

import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.logic.DatabaseOperator;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by Hermes235 on 28.4.2017.
 */
public class IngredientEditController implements Initializable {
    protected static final String BACKGROUND_NORMAL = "-fx-text-box-border: grey";
    static final String BACKGROUND_RED = "-fx-background-color: red";
    private static final Logger LOG = Logger.getLogger(IngredientEditController.class.getName());
    private static final DatabaseOperator OPERATOR = DatabaseOperator.OPERATOR;
    private static int selected = -1;
    @FXML public TextField txtName;
    @FXML public TextField txtUnit;
    @FXML public TextField txtPrice;
    @FXML public TextField txtAmount;
    @FXML public Label lblUnits;
    @FXML public DatePicker dpExpiration;
    @FXML public Button btnSave;
    @FXML public Button btnBack;
    @FXML public Button btnPrevious;
    @FXML public Button btnNext;
    private boolean changed = false;
    
    static void setSelected(int selected) {
        IngredientEditController.selected = selected;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUnit.textProperty().addListener((observable, oldValue, newValue) -> lblUnits.setText(newValue));
        setFields();
    }
    
    private void setFields() {
        if (selected == -1) {
            btnPrevious.disableProperty().setValue(true);
            btnNext.disableProperty().setValue(true);
            return;
        }
        if (OPERATOR.isFirstIngredient(selected)) {
            btnPrevious.disableProperty().setValue(true);
        } else {
            btnPrevious.disableProperty().setValue(false);
        }
        if (OPERATOR.isLastIngredient(selected)) {
            btnNext.disableProperty().setValue(true);
            btnPrevious.requestFocus();
        } else {
            btnNext.disableProperty().setValue(false);
        }
        Ingredient selectedIngredient = OPERATOR.getIngredient(selected);
        txtName.setText(selectedIngredient.getName());
        txtAmount.setText(String.valueOf(selectedIngredient.getAmount()));
        dpExpiration.setValue(selectedIngredient.getExpirationDate().toLocalDate());
        txtUnit.setText(selectedIngredient.getUnit());
        txtPrice.setText(String.valueOf(selectedIngredient.getPricePerUnit()));
        changed = false;
        btnSave.disableProperty().setValue(true);
    }
    
    public void somethingChanged(ActionEvent ignored) {
        changed = true;
        btnSave.disableProperty().setValue(false);
    }
    
    private void switchToMainForm(@Nonnull Event event) throws IOException {
        Parent homePageParent = FXMLLoader.load(getClass().getResource("/gui-forms/main.fxml"));
        Scene homePageScene = new Scene(homePageParent);
        Stage appStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        TabPane tabPane = (TabPane) homePageScene.lookup("#tpMainPane");
        tabPane.getSelectionModel().select(1);
        ListView listView = (ListView) homePageScene.lookup("#lwIngredients");
        listView.getSelectionModel().select(selected);
        appStage.setScene(homePageScene);
        appStage.show();
    }
    
    public void btnNextPressed(ActionEvent ignored) {
        synchronized (this) {
            ++selected;
        }
        setFields();
    }
    
    public void btnPreviousPressed(ActionEvent ignored) {
        synchronized (this) {
            --selected;
        }
        setFields();
    }
    
    public void btnBackPressed(@Nonnull ActionEvent event) throws IOException {
        if (changed) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved changes");
            alert.setContentText("You have not saved your changes! Are you sure you want to exit?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent() && (result.get() == ButtonType.OK))) {
                switchToMainForm(event);
            } else {
                btnSave.requestFocus();
            }
        } else {
            switchToMainForm(event);
        }
    }
    
    public void btnSavePressed(ActionEvent event) {
        Stream.of(txtAmount, txtName, txtPrice, txtUnit, dpExpiration).forEach(field -> field.setStyle(BACKGROUND_NORMAL));
        if (!checkFieldValidity()) {
            LOG.info(() -> "Invalid values filled");
            return;
        }
        
        if (selected == -1) {
            synchronized (this) {
                selected = OPERATOR.addIngredient(txtName.getText(), txtUnit.getText(), BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                                                  Double.parseDouble(txtAmount.getText()), java.sql.Date.valueOf(dpExpiration.getValue()));
            }
            if (selected == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Couldn't save to the data");
                alert.setContentText("An error has occurred trying to save to the data! Contact creator!");
                alert.showAndWait();
            } else {
                changed = false;
                btnSave.disableProperty().setValue(true);
            }
            
        } else {
            int result = OPERATOR.editIngredient(selected, txtName.getText(), txtUnit.getText(), BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())),
                                                 Double.parseDouble(txtAmount.getText()), java.sql.Date.valueOf(dpExpiration.getValue()));
            if (result == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Couldn't save to the data");
                alert.setContentText("An error has occurred trying to save to the data! Contact creator!");
                alert.showAndWait();
            } else {
                changed = false;
                synchronized (this) {
                    selected = result;
                }
                btnSave.disableProperty().setValue(true);
            }
        }
    }
    
    private boolean checkFieldValidity() {
        /* It's necessary to use & instead of && because with & all functions will be called and all invalid field will be set to red*/
        return isEverythingFilled() & isPickedValidDate() & isAmountCorrect();
    }
    
    private boolean isPickedValidDate() {
        final LocalDate pickedDate = dpExpiration.getValue();
        if (pickedDate == null || pickedDate.isBefore(LocalDate.now())) {
            dpExpiration.setStyle(BACKGROUND_RED);
            return false;
        }
        return true;
    }
    
    private boolean isAmountCorrect() {
        boolean result = false;
        
        try {
            final BigDecimal pickedPrice = new BigDecimal(txtPrice.getText());
            if (pickedPrice.signum() != 1) {
                txtPrice.setStyle(BACKGROUND_RED);
            } else {
                result = true;
            }
            // Couldn't parse given values
        } catch (Exception e) {
            LOG.log(Level.INFO, "User entered invalid data" + txtPrice.getText(), e);
            txtPrice.setStyle(BACKGROUND_RED);
            result = false;
        }
        
        try {
            final double pickedAmount = Double.parseDouble(txtAmount.getText());
            if (pickedAmount < 0) {
                txtAmount.setStyle(BACKGROUND_RED);
                result = false;
            }
            // Couldn't parse given values
        } catch (Exception e) {
            LOG.log(Level.INFO, "User entered invalid data" + txtAmount.getText(), e);
            txtAmount.setStyle(BACKGROUND_RED);
            result = false;
        }
        
        return result;
    }
    
    private boolean isEverythingFilled() {
        final boolean[] everythingFilled = {true};
        Stream.of(txtAmount, txtName, txtPrice, txtUnit).filter(field -> field.getText().trim().isEmpty()).forEach(field -> {
            field.setStyle(BACKGROUND_RED);
            everythingFilled[0] = false;
        });
        return everythingFilled[0];
    }
}
