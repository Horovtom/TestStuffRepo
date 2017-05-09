package com.cvut.fel.horovtom.data.dummyDAO;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Hermes235 on 27.4.2017.
 */
public class DummyIngredientDAO implements DAO<Ingredient> {
    private static final Logger LOG = Logger.getLogger(DummyIngredientDAO.class.getName());
    @Nonnull static Ingredient brambory = new Ingredient();
    @Nonnull static Ingredient maso = new Ingredient();
    @Nonnull static Ingredient hrach = new Ingredient();
    @Nonnull private static ArrayList<Ingredient> ingredients = new ArrayList<>();
    
    public DummyIngredientDAO() {
        brambory.setAmount(2);
        maso.setAmount(0.5);
        hrach.setAmount(0.2);
        brambory.setExpirationDate(new Date(System.currentTimeMillis()));
        maso.setExpirationDate(new Date(System.currentTimeMillis()));
        hrach.setExpirationDate(new Date(System.currentTimeMillis()));
        brambory.setIdIngredient(1);
        maso.setIdIngredient(2);
        hrach.setIdIngredient(3);
        brambory.setName("Brambory");
        maso.setName("Maso");
        hrach.setName("Hrach");
        brambory.setPricePerUnit(BigDecimal.valueOf(30));
        maso.setPricePerUnit(BigDecimal.valueOf(120));
        hrach.setPricePerUnit(BigDecimal.valueOf(50));
        brambory.setUnit("kg");
        maso.setUnit("kg");
        hrach.setUnit("kg");
        
        ingredients.add(hrach);
        ingredients.add(maso);
        ingredients.add(brambory);
    }
    
    @Nonnull
    @Override
    public List<Ingredient> list() {
        return ingredients;
    }
    
    @Nonnull
    @Override
    public Ingredient save(@Nonnull Ingredient entity) {
        LOG.info("Saving Ingredient: " + entity);
        ingredients.add(entity);
        return entity;
    }
    
    @Override
    public void delete(@Nonnull Serializable id) {
        LOG.info("Not implemented! Deleting Ingredient: " + id);
    }
    
    @Nullable
    @Override
    public Ingredient find(@Nonnull Serializable id) {
        LOG.info("Looking for Ingredient: " + id);
        
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getIdIngredient() == (int) id) {
                return ingredient;
            }
        }
        
        return null;
    }
    
    @Nonnull
    @Override
    public Ingredient update(@Nonnull Ingredient entity) {
        LOG.info("Updating Ingredient: " + entity);
        
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getIdIngredient() == entity.getIdIngredient()) {
                return ingredient;
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
