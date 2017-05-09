package com.cvut.fel.horovtom.presentation;

import com.cvut.fel.horovtom.logic.DatabaseOperator;
import com.cvut.fel.horovtom.logic.ServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by Hermes235 on 25.4.2017.
 */
public class MainController implements Initializable {
    private static final Logger LOG = Logger.getLogger(MainController.class.getName());
    private static final DatabaseOperator OPERATOR = DatabaseOperator.OPERATOR;
    
    
    @FXML public Button btnResetFilters;
    @FXML public Button btnFilterFoods;
    @FXML public Button btnAddRec;
    @FXML public Button btnCookFilter;
    @FXML public Button btnFilterTypes;
    @FXML private Button btnDisplayRec;
    @FXML private ComboBox<String> cmbTypes;
    @FXML private ChoiceBox<String> chbRecipeList;
    @FXML private Button btnAddIngredient;
    @FXML private Button btnEditIngredient;
    @FXML private ListView<String> lwIngredients;
    @FXML private ComboBox<String> cmbFoods;
    @FXML private Button btnExpired;
    @FXML private Button btnDeleteIngredient;
    
    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        init();
        
        lwIngredients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnEditIngredient.disableProperty().setValue(false);
            btnDeleteIngredient.disableProperty().setValue(false);
        });
        cmbFoods.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> btnFilterFoods.disableProperty().setValue(false)));
        cmbTypes.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> btnFilterTypes.disableProperty().setValue(false)));
        chbRecipeList.getSelectionModel()
                     .selectedItemProperty()
                     .addListener(((observable, oldValue, newValue) -> btnDisplayRec.disableProperty().setValue(false)));
        
        LOG.info("Done loading...");
    }
    
    
    private void init() {
        cmbTypes.setItems(FXCollections.observableArrayList(OPERATOR.getFoodTypes()));
        cmbTypes.getSelectionModel().clearSelection();
        cmbFoods.setItems(FXCollections.observableArrayList(OPERATOR.getFoods()));
        cmbFoods.getSelectionModel().clearSelection();
        chbRecipeList.setItems(FXCollections.observableArrayList(OPERATOR.getRecipes()));
        chbRecipeList.getSelectionModel().clearSelection();
        lwIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredients()));
        lwIngredients.getSelectionModel().clearSelection();
        btnDeleteIngredient.disableProperty().setValue(true);
        btnEditIngredient.disableProperty().setValue(true);
        btnDisplayRec.disableProperty().setValue(true);
        btnFilterFoods.disableProperty().setValue(true);
        btnFilterTypes.disableProperty().setValue(true);
    }
    
    public void btnFilterTypesPressed(Event event) {
        if (cmbTypes.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }
        LOG.info("btnFilterTypes pressed!");
        cmbFoods.setItems(FXCollections.observableArrayList(OPERATOR.getFoodsWithType(cmbTypes.getSelectionModel().getSelectedIndex())));
        chbRecipeList.setItems(FXCollections.observableArrayList(OPERATOR.getRecipesOfFoodTypes(cmbTypes.getSelectionModel().getSelectedIndex())));
    }
    
    public void btnFilterFoodsPressed(Event event) {
        if (cmbFoods.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }
        LOG.info("btnFilterFoods pressed!");
        chbRecipeList.setItems(FXCollections.observableArrayList(OPERATOR.getRecipesOfFood(cmbFoods.getSelectionModel().getSelectedIndex())));
    }
    
    public void btnResetFiltersPressed(Event event) {
        init();
    }
    
    private void switchToForm(String path, @Nonnull Event event) throws IOException {
        LOG.info("Switching to: " + path + " form!");
        Parent homePageParent = FXMLLoader.load(getClass().getResource(path));
        Scene homePageScene = new Scene(homePageParent);
        Stage appStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        
        appStage.setScene(homePageScene);
        appStage.show();
    }
    
    private void switchToIngredientForm(@Nonnull Event event) throws IOException {
        switchToForm("/gui-forms/ingredient-edit.fxml", event);
    }
    
    private void switchToRecipeForm(@Nonnull Event event) throws IOException {
        switchToForm("/gui-forms/recipe-edit.fxml", event);
    }
    
    public void btnAddIngredientPressed(@Nonnull Event event) throws IOException {
        IngredientEditController.setSelected(-1);
        switchToIngredientForm(event);
    }
    
    public void btnEditIngredientPressed(@Nonnull Event event) throws IOException {
        IngredientEditController.setSelected(lwIngredients.getSelectionModel().getSelectedIndex());
        switchToIngredientForm(event);
    }
    
    public void btnDeleteIngredientPressed(Event event) {
        OPERATOR.deleteIngredient(lwIngredients.getSelectionModel().getSelectedIndex());
        init();
        btnAddIngredient.requestFocus();
    }
    
    public void btnExpiredPressed(Event ignored) {
        final String currentStyle = btnExpired.getStyle();
        if ("-fx-background-color: green".equals(currentStyle)) {
            btnExpired.setStyle("-fx-background-color: lightgrey");
            lwIngredients.setItems(FXCollections.observableArrayList(OPERATOR.getIngredients()));
        } else {
            btnExpired.setStyle("-fx-background-color: green");
            lwIngredients.setItems(FXCollections.observableArrayList(new ServiceImpl().getNamesOfExpiredIngredients()));
        }
        lwIngredients.getSelectionModel().clearSelection();
    }
    
    public void btnCookFilterPressed(Event event) {
        //TODO: IMPLEMENT
    }
    
    public void btnAddRecPressed(@Nonnull Event event) throws IOException {
        OPERATOR.setSelectedRecipeIndex(-1);
        switchToRecipeForm(event);
    }
    
    public void btnDisplayRecPressed(@Nonnull Event event) throws IOException {
        OPERATOR.setSelectedRecipeIndex(chbRecipeList.getSelectionModel().getSelectedIndex());
        switchToRecipeForm(event);
    }
}
