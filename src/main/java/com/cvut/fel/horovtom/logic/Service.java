package com.cvut.fel.horovtom.logic;

import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides logical operations on data like obtaing specifical values
 *
 * @author Tomáš Hamsa on 21.04.2017.
 */
public interface Service {
    
    /**
     * Obtains all ingredients present in fridge from data. This method uses some sql queries that make take a long time. The longest time is taken by
     * connecting to data the actual finding is very short.
     *
     * @return Stream of all ingredients present in fridge. The Stream might be empty.
     */
    @Deprecated
    @Nonnull
    Stream<Ingredient> getIngredientsFromFridge();
    
    /**
     * Obtains all recipes from the data and filter them so it just contains recipes that can be cooked from present ingredients. This method must
     * connect to data so the call can take very long.
     *
     * @return Stream of all cookable recipes. The stream might be empty
     */
    @Deprecated
    @Nonnull
    Stream<Recipe> getPossibleRecipes();
    
    
    List<String> getNamesOfExpiredIngredients();
}
