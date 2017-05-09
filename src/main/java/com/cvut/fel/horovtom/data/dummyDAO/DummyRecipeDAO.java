package com.cvut.fel.horovtom.data.dummyDAO;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Hermes235 on 27.4.2017.
 */
public class DummyRecipeDAO implements DAO<Recipe> {
    private static final Logger LOG = Logger.getLogger(DummyRecipeDAO.class.getName());
    @Nonnull static Recipe rizekPostaru = new Recipe();
    @Nonnull static Recipe rizekPonovu = new Recipe();
    @Nonnull static Recipe knedloRecept = new Recipe();
    @Nonnull private static ArrayList<Recipe> recipes = new ArrayList<>();
    
    public DummyRecipeDAO() {
        rizekPonovu.setFood(DummyFoodDAO.rizek);
        rizekPostaru.setFood(DummyFoodDAO.rizek);
        knedloRecept.setFood(DummyFoodDAO.knedlo);
        rizekPostaru.setName("Řízek postaru");
        rizekPonovu.setName("Řízek ponovu");
        knedloRecept.setName("Knedlo vepřo normálně");
        rizekPostaru.setDescription("Uděláte ho postaru");
        rizekPonovu.setDescription("Uděláte ho ponovu");
        knedloRecept.setDescription("Normálně ho prostě uděláte");
        rizekPostaru.setDifficulty(DummyDifficultyDAO.hard);
        rizekPonovu.setDifficulty(DummyDifficultyDAO.easy);
        knedloRecept.setDifficulty(DummyDifficultyDAO.easy);
        rizekPostaru.setIdRecipe(1);
        rizekPonovu.setIdRecipe(2);
        knedloRecept.setIdRecipe(3);
        Map<Ingredient, Double> postaru = new LinkedHashMap<>();
        postaru.put(DummyIngredientDAO.maso, 2.0);
        postaru.put(DummyIngredientDAO.hrach, 2.0);
        rizekPostaru.setIngredients(postaru);
        Map<Ingredient, Double> ponovu = new LinkedHashMap<>();
        ponovu.put(DummyIngredientDAO.maso, 1.0);
        rizekPonovu.setIngredients(ponovu);
        Map<Ingredient, Double> knedlo = new LinkedHashMap<>();
        knedlo.put(DummyIngredientDAO.brambory, 0.5);
        knedloRecept.setIngredients(knedlo);
        rizekPostaru.setPeopleAmount((short) 3);
        rizekPonovu.setPeopleAmount((short) 2);
        knedloRecept.setPeopleAmount((short) 1);
        
        recipes.add(rizekPonovu);
        recipes.add(rizekPostaru);
        recipes.add(knedloRecept);
    }
    
    @Nonnull
    @Override
    public List<Recipe> list() {
        return recipes;
    }
    
    @Nonnull
    @Override
    public Recipe save(@Nonnull Recipe entity) {
        LOG.info("Saving Recipe: " + entity);
        recipes.add(entity);
        return entity;
    }
    
    @Override
    public void delete(@Nonnull Serializable id) {
        LOG.info("Not Implemented! Deleting Recipe: " + id);
    }
    
    @Nullable
    @Override
    public Recipe find(@Nonnull Serializable id) {
        LOG.info("Finding Recipe: " + id);
        for (Recipe recipe : recipes) {
            if (recipe.getIdRecipe() == (int) id) {
                return recipe;
            }
        }
        return null;
        
    }
    
    @Nonnull
    @Override
    public Recipe update(@Nonnull Recipe entity) {
        LOG.info("Updating Recipe: " + entity);
        
        for (Recipe recipe : recipes) {
            if (recipe.getIdRecipe() == entity.getIdRecipe()) {
                return recipe;
            }
        }
        return null;
    }
    
    @Nonnull
    @Override
    public List<Recipe> listCookableRecipes() {
        return null;
    }
}
