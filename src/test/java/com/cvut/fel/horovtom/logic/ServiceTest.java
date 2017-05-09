package com.cvut.fel.horovtom.logic;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.dao.DAOImpl;
import com.cvut.fel.horovtom.data.dao.GenericDAOTest;
import com.cvut.fel.horovtom.data.model.Difficulty;
import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tomáš Hamsa on 22.04.2017.
 */
public class ServiceTest {
    @Test
    public void testGetIngredientsFromFridge() throws Exception {
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        
        //Create and save first ingredient
        final Ingredient ingreadiantA = new Ingredient().setAmount(1)
                                                        .setExpirationDate(new Date(789456))
                                                        .setName(GenericDAOTest.getRandomString())
                                                        .setPricePerUnit(BigDecimal.TEN)
                                                        .setUnit("testUNit");
        Assert.assertEquals(ingredientDAO.save(ingreadiantA), ingreadiantA);
        
        //Create and save second ingredient
        final Ingredient ingreadiantB = new Ingredient().setAmount(2)
                                                        .setExpirationDate(new Date(9456))
                                                        .setName(GenericDAOTest.getRandomString())
                                                        .setPricePerUnit(BigDecimal.ONE)
                                                        .setUnit("testUnit2");
        Assert.assertEquals(ingredientDAO.save(ingreadiantB), ingreadiantB);
        
        //Create and save difficulty
        final Difficulty newDifficulty = new Difficulty().setLevel(GenericDAOTest.getRandomString());
        Assert.assertEquals(new DAOImpl<>(Difficulty.class).save(newDifficulty), newDifficulty);
        
        final Collection<Ingredient> result = new ServiceImpl().getIngredientsFromFridge().collect(Collectors.toList());
        final Collection<Ingredient> expected = Arrays.asList(ingreadiantA, ingreadiantB);
        
        Assert.assertTrue(result.containsAll(expected) && !result.contains(newDifficulty));
    }
    
    @Test
    public void testGetPossibleRecipes() throws Exception {
        String string = "January 2, 2040";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        java.util.Date javaDate = format.parse(string);
        Date date = new Date(javaDate.getTime());
        
        final Ingredient savedIngredient = new Ingredient().setExpirationDate(date)
                                                           .setName(GenericDAOTest.getRandomString())
                                                           .setPricePerUnit(BigDecimal.TEN)
                                                           .setUnit("unit")
                                                           .setAmount(Double.MAX_VALUE);
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        ingredientDAO.save(savedIngredient);
        
        final Ingredient expiredIngredient = new Ingredient().setAmount(30000)
                                                             .setExpirationDate(new Date(0))
                                                             .setName(GenericDAOTest.getRandomString())
                                                             .setPricePerUnit(BigDecimal.ONE)
                                                             .setUnit("Unit");
        ingredientDAO.save(expiredIngredient);
        
        final Recipe cookableRecipe = new Recipe().setDescription(GenericDAOTest.getRandomString())
                                                  .setName(GenericDAOTest.getRandomString())
                                                  .setPeopleAmount((short) 1);
        
        Map<Ingredient, Double> toPass = new HashMap<>();
        toPass.put(savedIngredient, 0.0);
        cookableRecipe.setIngredients(toPass);
        
        final DAO<Recipe> recipeDAO = new DAOImpl<>(Recipe.class);
        recipeDAO.save(cookableRecipe);
        
        final Recipe recipeWithExpired = new Recipe().setDescription(GenericDAOTest.getRandomString())
                                                     .setName(GenericDAOTest.getRandomString())
                                                     .setPeopleAmount((short) 5);
        Map<Ingredient, Double> expired = new HashMap<>();
        expired.put(expiredIngredient, 2.0);
        recipeWithExpired.setIngredients(expired);
        recipeDAO.save(recipeWithExpired);
        
        
        final Collection<Recipe> result = ingredientDAO.listCookableRecipes();
        Assert.assertTrue(result.contains(cookableRecipe) && !result.contains(recipeWithExpired));
    }
}