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
public class DummyFoodTypeDAO implements DAO<FoodType> {
    private static final Logger LOG = Logger.getLogger(DummyFoodTypeDAO.class.getName());
    
    @Nonnull static FoodType hlavni = new FoodType();
    @Nonnull static FoodType polevka = new FoodType();
    
    @Nonnull private static ArrayList<FoodType> foodTypes = new ArrayList<>();
    
    public DummyFoodTypeDAO() {
        List<Food> foods = new ArrayList<>();
        foods.add(DummyFoodDAO.knedlo);
        foods.add(DummyFoodDAO.rizek);
        hlavni.setFoods(foods);
        polevka.setFoods(new ArrayList<>());
        hlavni.setIdFoodtype(1);
        polevka.setIdFoodtype(2);
        hlavni.setTypefood("Hlavní jídlo");
        polevka.setTypefood("Polévka");
        foodTypes.add(hlavni);
        foodTypes.add(polevka);
    }
    
    @Nonnull
    @Override
    public List<FoodType> list() {
        
        return foodTypes;
    }
    
    @Nonnull
    @Override
    public FoodType save(@Nonnull FoodType entity) {
        LOG.info("Saving FoodType: " + entity);
        foodTypes.add(entity);
        return entity;
    }
    
    @Override
    public void delete(@Nonnull Serializable id) {
        LOG.info("Not Implemented! Deleting FoodType: " + id);
    }
    
    @Nullable
    @Override
    public FoodType find(@Nonnull Serializable id) {
        LOG.info("Looking for FoodType: " + id);
        for (FoodType foodType : foodTypes) {
            if (foodType.getIdFoodtype() == (int) id) {
                return foodType;
            }
        }
        return null;
    }
    
    @Nonnull
    @Override
    public FoodType update(@Nonnull FoodType entity) {
        LOG.info("Updating FoodType: " + entity);
        for (FoodType foodType : foodTypes) {
            if (foodType.getIdFoodtype() == entity.getIdFoodtype()) {
                return foodType;
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
