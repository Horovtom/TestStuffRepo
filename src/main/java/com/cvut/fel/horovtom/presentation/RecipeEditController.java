package com.cvut.fel.horovtom.presentation;

import com.cvut.fel.horovtom.logic.DatabaseOperator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by Hermes235 on 1.5.2017.
 */
public class RecipeEditController implements Initializable {
    private static final Logger LOG = Logger.getLogger(RecipeEditController.class.getName());
    private static final DatabaseOperator OPERATOR = DatabaseOperator.OPERATOR;
    public TextField txtName;
    public TextField txtPeopleAmount;
    public ChoiceBox<String> chbDifficulty;
    public Button btnAddDiff;
    public Button btnEditDiff;
    public Button btnDelDiff;
    public ChoiceBox<String> chbFood;
    public Button btnAddFood;
    public Button btnEditFood;
    public Button btnRemoveFood;
    public ListView<String> lwIngredients;
    public ChoiceBox<String> chbIngredients;
    public Button btnAddToRec;
    public Button btnDelFromRec;
    public Button btnAddIngredient;
    public TextArea txtDesc;
    public Button btnBack;
    public Button btnPrevious;
    public Button btnNext;
    public Button btnSave;
    public TextField txtAmount;
    public Label lblUnit;
    private boolean changed = false;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chbIngredients.getSelectionModel()
                      .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    lblUnit.setText(OPERATOR.getUnitOfIngredient(newValue.intValue()));
                    changed();
                });
        chbDifficulty.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    OPERATOR.setSelectedRecipeDifficulty(newValue.intValue());
                    changed();
                });
        chbFood.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    OPERATOR.setSelectedRecipeFood(newValue.intValue());
                    changed();
                });
        txtPeopleAmount.textProperty().addListener((observable, oldValue, newValue) -> changed());
        txtName.textProperty().addListener((observable, oldValue, newValue) -> changed());
        txtDesc.textProperty().addListener((observable, oldValue, newValue) -> changed());


        setFields();
    }

    private void changed() {
        changed = true;
        btnSave.disableProperty().setValue(false);
    }

    private void unchanged() {
        changed = false;
        btnSave.disableProperty().setValue(true);
    }
    
    private void setFields() {
        loadData();
        if (OPERATOR.isNewRecipe()) {
            btnPrevious.disableProperty().setValue(true);
            btnNext.disableProperty().setValue(true);
        } else {

            if (OPERATOR.isFirstRecipe()) {
                btnPrevious.disableProperty().setValue(true);
            } else {
                btnPrevious.disableProperty().setValue(false);
            }
            if (OPERATOR.isLastRecipe()) {
                btnNext.disableProperty().setValue(true);
                btnPrevious.requestFocus();
            } else {
                btnNext.disableProperty().setValue(false);
            }
        }

        txtDesc.setText(OPERATOR.getSelectedRecipe().getDescription());
        txtName.setText(OPERATOR.getSelectedRecipe().getName());
        txtPeopleAmount.setText(String.valueOf(OPERATOR.getSelectedRecipe().getPeopleAmount()));
        chbDifficulty.getSelectionModel().select(OPERATOR.getDifficultySelectionIndex());
        chbFood.getSelectionModel().select(OPERATOR.getFoodSelectionIndex());
        lwIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredientsInRecipe()));
        unchanged();
    }
    
    /**
     * Loads data about Ingredients, Difficulties and Foods from database
     */
    private void loadData() {
        chbIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredients()));
        chbDifficulty.setItems(FXCollections.observableArrayList(OPERATOR.getDifficulties()));
        chbFood.setItems(FXCollections.observableArrayList(OPERATOR.getFoods()));
    }
    
    
    //region Ingredient tab

    private boolean txtAmountValid() {
        try {
            double d = Double.parseDouble(txtAmount.getText());
            return d > 0;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    /**
     * {@link #btnAddToRec}
     * Ingredients tab
     * Adds ingredient to recipe
     */
    public void btnAddToRecPressed(ActionEvent actionEvent) {
        if (!txtAmountValid()) {
            txtAmount.setStyle(IngredientEditController.BACKGROUND_RED);
            return;
        } else txtAmount.setStyle(IngredientEditController.BACKGROUND_NORMAL);
        double amount = Double.parseDouble(txtAmount.getText());

        OPERATOR.addIngredientToRecipe(chbIngredients.getSelectionModel().getSelectedIndex(), amount);
        lwIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredientsInRecipe()));
    }

    /**
     * {@link #btnDelFromRec}
     * Ingredients tab
     * Removes selected ingredient from recipe
     */
    public void btnDelFromRecPressed(ActionEvent actionEvent) {
        OPERATOR.removeIngredientFromCurrRecipe(lwIngredients.getSelectionModel().getSelectedIndex());
        lwIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredientsInRecipe()));
        changed();
    }
    
    /**
     * {@link #btnAddIngredient}
     * Ingredients tab
     * Show form for creating new ingredient
     */
    public void btnAddIngredientPressed(ActionEvent actionEvent) {
        //TODO: [OPTIONAL] IMPLEMENT
    }
    
    //endregion
    
    //region Food tab
    
    /**
     * {@link #btnRemoveFood}
     * Food tab
     * Removes selected food from database
     */
    public void btnRemoveFoodPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    /**
     * {@link #btnEditFood}
     * Food tab
     * Edit selected food in database
     */
    public void btnEditFoodPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    /**
     * {@link #btnAddFood}
     * Food tab
     * Add new food to database
     */
    public void btnAddFoodPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    //endregion
    
    //region General tab
    
    /**
     * {@link #btnAddDiff}
     * General tab
     * Adds new Difficulty to database
     */
    public void btnAddDiffPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    /**
     * {@link #btnEditDiff}
     * General tab
     * Edit selected difficulty in database
     */
    public void btnEditDiffPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    /**
     * {@link #btnDelDiff}
     * General tab
     * Delete selected difficulty from database
     */
    public void btnDelDiffPressed(ActionEvent actionEvent) {
        //TODO: IMPLEMENT
    }
    
    //endregion
    
    //region Bottom Menu
    
    /**
     * {@link #btnBack}
     * Bottom menu
     * Goes back to the Main form
     */
    public void btnBackPressed(@Nonnull ActionEvent actionEvent) throws IOException {
        if (checkUnsavedChanges()) {
            switchToMainForm(actionEvent);
        } else {
            btnSave.requestFocus();
        }
    }
    
    /**
     * If there are any unsaved changes: Marked by {@link #changed}, ask user for confirmation and return the result
     *
     * @return if it should do its thing
     */
    private boolean checkUnsavedChanges() {
        if (!changed) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved changes");
        alert.setContentText("You have unsaved changes, do you want to quit anyway?");
        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent() && (result.get() == ButtonType.OK));
    }
    
    private void switchToMainForm(@Nonnull Event event) throws IOException {
        LOG.info(() -> "Switching to main form!");
        Parent homePageParent = FXMLLoader.load(getClass().getResource("/gui-forms/main.fxml"));
        Scene homePageScene = new Scene(homePageParent);
        Stage appStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        
        appStage.setScene(homePageScene);
        appStage.show();
    }
    
    /**
     * {@link #btnPrevious}
     * Bottom menu
     * Switches current recipe to the previous
     */
    public void btnPreviousPressed(ActionEvent actionEvent) {
        if (checkUnsavedChanges()) {
            OPERATOR.previousRecipe();
            setFields();
        } else {
            btnSave.requestFocus();
        }
    }
    
    /**
     * {@link #btnNext}
     * Bottom menu
     * Switches current recipe to the next
     */
    public void btnNextPressed(ActionEvent actionEvent) {
        if (checkUnsavedChanges()) {
            OPERATOR.nextRecipe();
            setFields();
        } else {
            btnSave.requestFocus();
        }
    }
    
    /**
     * {@link #btnNext}
     * Bottom menu
     * Save current recipe to the database
     */
    public void btnSavePressed(ActionEvent actionEvent) throws IOException {
        Stream.of(txtName, txtPeopleAmount, txtDesc, chbDifficulty, chbFood, lwIngredients)
              .forEach(field -> field.setStyle(IngredientEditController.BACKGROUND_NORMAL));
        if (!checkFields()) {
            LOG.info(() -> "User provided invalid information");
            return;
        }
        saveFields();
        //switchToMainForm(actionEvent);
    }
    
    /**
     * Used by: {@link #btnSavePressed(ActionEvent)}
     * Saves fields in form to the database
     */
    private void saveFields() {
        OPERATOR.setSelectedRecipeFields(txtName.getText(), txtDesc.getText(), Short.parseShort(txtPeopleAmount.getText()));
        if (!OPERATOR.saveRecipe()) errorSaving();
        unchanged();
    }
    
    private void errorSaving() {
        LOG.severe(() -> "There was an error saving in the database!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error saving");
        alert.setContentText("An error occurred while saving in the database! Contact developer!");
        alert.showAndWait();
    }
    
    /**
     * Used by: {@link #btnSavePressed(ActionEvent)}
     * Checks fields in the whole form, highlights errors if needed
     *
     * @return true if form is OK
     */
    private boolean checkFields() {
        return isTextFieldsFiled() & isAmountPositive();
    }

    private boolean isAmountPositive() {
            try {
                final short value = Short.parseShort(txtPeopleAmount.getText());
                if (value <= 0) {
                    LOG.info("This recipe would be for <=0 ppl!");
                    txtPeopleAmount.setStyle(IngredientEditController.BACKGROUND_RED);
                    return false;
                }
            } catch (@Nonnull NumberFormatException | NullPointerException e) {
                LOG.log(Level.INFO, "User provided unparsable number " + txtPeopleAmount.getText(), e);
                txtPeopleAmount.setStyle(IngredientEditController.BACKGROUND_RED);
            }
        return true;
    }

    @Deprecated
    private boolean isIngredientPicked() {
        final boolean empty = Stream.of(lwIngredients.getItems()).anyMatch(item -> item == null || item.isEmpty());
        if (empty) {
            lwIngredients.setStyle(IngredientEditController.BACKGROUND_RED);
        }
        return empty;
    }
    
    private boolean isTextFieldsFiled() {
//        return !Stream.of(txtName, txtDesc).filter(field -> field.getText().trim().isEmpty()).allMatch(field -> {
//            field.setStyle(IngredientEditController.BACKGROUND_RED);
//            return true;
//        });
        boolean toReturn = true;
        if (txtName.getText().isEmpty()) {
            txtName.setStyle(IngredientEditController.BACKGROUND_RED);
            toReturn = false;
        }
        if (txtDesc.getText().isEmpty()) {
            txtDesc.setStyle(IngredientEditController.BACKGROUND_RED);
            toReturn = false;
        }
        return toReturn;

    }
    
    //endregion
}
