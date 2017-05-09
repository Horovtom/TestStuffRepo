package com.cvut.fel.horovtom.presentation;

import javafx.scene.control.TextArea;

import javax.annotation.Nonnull;

/**
 * Presenter that shows data in UI
 *
 * @author Tomáš Hamsa on 22.04.2017.
 */
public interface Presenter {
    
    /**
     * Shows all ingredients present in the fridge in UI
     */
    void showIngredients();
    
    void setText(final @Nonnull TextArea text);
    
    /**
     * Shows all recipes for which we have enough ingredients in UI
     */
    void showCookableRecipes();
}
