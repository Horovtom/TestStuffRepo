package com.cvut.fel.horovtom.logic;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.dao.DAOImpl;
import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tomáš Hamsa on 21.04.2017.
 */
public class ServiceImpl implements Service {
    
    @Deprecated
    @Override
    @Nonnull
    public final Stream<Ingredient> getIngredientsFromFridge() {
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        return ingredientDAO.list().stream();
    }
    
    @Deprecated
    @Override
    @Nonnull
    public final Stream<Recipe> getPossibleRecipes() {
        final DAO<Recipe> recipeDAO = new DAOImpl<>(Recipe.class);
        final Date dateNow = new Date();
        //Collection of ingredients that have expiration date after now
        final Collection<Ingredient> validIngredients = getIngredientsFromFridge().filter(ingredient -> ingredient.getExpirationDate().after(dateNow))
                                                                                  .collect(Collectors.toList());
        
        return recipeDAO.list().stream().filter(recipe -> validIngredients.containsAll(recipe.getIngredients().keySet()));
    }
    
    @Override
    public final List<String> getNamesOfExpiredIngredients() {
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        final Date dateNow = new Date();
        return ingredientDAO.list()
                            .stream()
                            .filter(ingredient -> ingredient.getAmount() > 0)
                            .filter(ingredient -> ingredient.getExpirationDate().before(dateNow))
                            .map(Ingredient::getName)
                            .sorted()
                            .collect(Collectors.toList());
    }
}
