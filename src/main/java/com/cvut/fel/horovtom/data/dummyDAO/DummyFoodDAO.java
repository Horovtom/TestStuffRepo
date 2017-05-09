package com.cvut.fel.horovtom.data.dummyDAO;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.model.Food;
import com.cvut.fel.horovtom.data.model.FoodType;
import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by Hermes235 on 27.4.2017.
 */
public class DummyFoodDAO implements DAO<Food> {
    
    private static final Logger LOG = Logger.getLogger(DummyFoodDAO.class.getName());
    
    @Nonnull static Food knedlo = new Food();
    @Nonnull static Food rizek = new Food();
    @Nonnull private static ArrayList<Food> foods = new ArrayList<>();
    
    public DummyFoodDAO() {
        knedlo.setIdFood(1);
        rizek.setIdFood(2);
        knedlo.setName("Knedlo vepřo zelo");
        rizek.setName("Vídeňský řízek");
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(DummyRecipeDAO.knedloRecept);
        knedlo.setRecipes(recipes);
        List<Recipe> rizky = new ArrayList<>();
        rizky.add(DummyRecipeDAO.rizekPonovu);
        rizky.add(DummyRecipeDAO.rizekPostaru);
        rizek.setRecipes(rizky);
        List<FoodType> types = new ArrayList<>();
        types.add(DummyFoodTypeDAO.hlavni);
        knedlo.setFoodTypes(types);
        List<FoodType> types2 = new ArrayList<>();
        types2.add(DummyFoodTypeDAO.hlavni);
        rizek.setFoodTypes(types2);
        
        foods.add(rizek);
        foods.add(knedlo);
    }
    
    @Nonnull
    @Override
    public List<Food> list() {
        return foods;
    }
    
    @Nonnull
    @Override
    public Food save(@Nonnull Food entity) {
        LOG.info("Saving Food: " + entity);
        foods.add(entity);
        return entity;
    }
    
    @Override
    public void delete(@Nonnull Serializable id) {
        LOG.info("Not ImplementedDeleting Food: " + id);
    }
    
    @Nullable
    @Override
    public Food find(@Nonnull Serializable id) {
        LOG.info("Looking for Food: " + id);
        for (Food food : foods) {
            if (food.getIdFood() == (int) id) {
                return food;
            }
        }
        return null;
    }
    
    @Nonnull
    @Override
    public Food update(@Nonnull Food entity) {
        LOG.info("Updating Food: " + entity);
        for (Food food : foods) {
            if (food.getIdFood() == entity.getIdFood()) {
                return food;
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