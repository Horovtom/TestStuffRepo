package com.cvut.fel.horovtom.presentation;

import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;
import com.cvut.fel.horovtom.logic.Service;
import com.cvut.fel.horovtom.logic.ServiceImpl;
import com.google.gson.Gson;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides presentation of found model in UI
 *
 * @author Tomáš Hamsa on 22.04.2017.
 */
class PresenterImpl implements Presenter {
    /**
     * Database logic that executes all queries on data
     */
    @Nonnull private final Service service;
    /**
     * Parses object into JSON so all fields are clearly visible
     */
    @Nonnull private final Gson jsonBuilder;
    /**
     * ServiceImpl that finds all cookable recipes in data and updates {@code text} shown in UI.
     */
    @Nonnull private final javafx.concurrent.Service cookableRecipes;
    /**
     * * ServiceImpl that finds all ingedients present in fridge from data and updates {@code text} shown in UI.
     */
    @Nonnull private final javafx.concurrent.Service allIngredients;
    /**
     * Text that is shown in UI window. All results from data should be set to this text so user can see them
     */
    private TextArea text;
    /**
     * Signalizes if data is processing some query. True if connection is progress, false otherwise
     */
    private volatile boolean databaseConnectingInProgress = false;
    
    PresenterImpl() {
        service = new ServiceImpl();
        jsonBuilder = new Gson();
        
        // Create services that run on separate thread
        cookableRecipes = createCookableRecipes();
        allIngredients = createAllIngredients();
    }
    
    
    /**
     * ServiceImpl that obtains all ingredients present in fridge. This is the only effective way of using multiple threads to update UI in javaFX. This method
     * basically creates instance of logic which executes
     * data query on separate thread so the UI doesn't freeze.
     *
     * @return ServiceImpl that obtains all ingredients form data and updates text in UI windows
     */
    private javafx.concurrent.Service createAllIngredients() {
        return new javafx.concurrent.Service() {
            @Nonnull
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Nullable
                    @Override
                    protected Void call() throws Exception {
                        databaseConnectingInProgress = true;
                        updateMessage("Please wait: Obtaining all ingredients from the data...");
                        final Stream<Ingredient> presentIngredients = service.getIngredientsFromFridge();
                        String allIngredients = presentIngredients.map(jsonBuilder::toJson).collect(Collectors.joining(System.lineSeparator()));
                        allIngredients = allIngredients.isEmpty() ? "No ingredients found" : allIngredients;
                        updateMessage("Ingredients:" + System.lineSeparator() + allIngredients);
                        databaseConnectingInProgress = false;
                        return null;
                    }
                };
            }
        };
    }
    
    /**
     * ServiceImpl that obtains all cookable recipes from data. This is the only effective way of using multiple threads to update UI in javaFX. This method
     * basically creates instance of logic which executes
     * data query on separate thread so the UI doesn't freeze.
     *
     * @return ServiceImpl that executes data query on separate thread and updates text in UI windows
     */
    private javafx.concurrent.Service createCookableRecipes() {
        return new javafx.concurrent.Service() {
            @Nonnull
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Nullable
                    @Override
                    protected Void call() throws Exception {
                        databaseConnectingInProgress = true;
                        updateMessage("Please wait: Obtaining all cookable recipes from the data...");
                        final Stream<Recipe> cookableRecipes = service.getPossibleRecipes();
                        String allRecipes = cookableRecipes.map(jsonBuilder::toJson).collect(Collectors.joining(System.lineSeparator()));
                        allRecipes = allRecipes.isEmpty() ? "No recipes found" : allRecipes;
                        updateMessage("Recipes:" + System.lineSeparator() + allRecipes);
                        databaseConnectingInProgress = false;
                        return null;
                    }
                };
            }
        };
    }
    
    @Override
    public final void showIngredients() {
        if (databaseConnectingInProgress) {
            return;
        }
        text.textProperty().bind(allIngredients.messageProperty());
        allIngredients.restart();
        
    }
    
    @Override
    public void setText(@Nonnull TextArea text) {
        this.text = text;
    }
    
    @Override
    public void showCookableRecipes() {
        if (databaseConnectingInProgress) {
            return;
        }
        text.textProperty().bind(cookableRecipes.messageProperty());
        cookableRecipes.restart();
    }
}
