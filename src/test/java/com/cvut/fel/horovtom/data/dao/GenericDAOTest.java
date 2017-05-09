package com.cvut.fel.horovtom.data.dao;

import com.cvut.fel.horovtom.data.model.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

/**
 * @author Tomáš Hamsa on 21.04.2017.
 */
public class GenericDAOTest {
    
    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }
    
    @Test(priority = -2)
    public void testSave() throws Exception {
        final DAO<Food> foodDAO = new DAOImpl<>(Food.class);
        final Food newfood = new Food().setName(getRandomString());
        Assert.assertEquals(foodDAO.save(newfood), newfood);
    }
    
    @Test(priority = -2)
    public void testSave2() throws Exception {
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        
        final Ingredient newIngredient = new Ingredient().setAmount(2.0)
                                                         .setExpirationDate(new Date(System.currentTimeMillis()))
                                                         .setName("Cibule" + getRandomString())
                                                         .setUnit("kg")
                                                         .setPricePerUnit(BigDecimal.valueOf(20));
        Assert.assertEquals(ingredientDAO.save(newIngredient), newIngredient);
        
        final DAO<Difficulty> difficultyIDAO = new DAOImpl<>(Difficulty.class);
        final Recipe newRecipe = new Recipe();
        final Difficulty difficulty = new Difficulty().setLevel("Easy" + getRandomString());
        Assert.assertEquals(difficultyIDAO.save(difficulty), difficulty);
        
        final FoodType foodType = new FoodType().setTypefood("Zelenina" + getRandomString());
        Assert.assertEquals(new DAOImpl<>(FoodType.class).save(foodType), foodType);
        
        
        final Food food = new Food().setName("Cibule naměkko" + getRandomString());
        List<FoodType> foodTypes = new ArrayList<>();
        foodTypes.add(foodType);
        food.setFoodTypes(foodTypes);
        Assert.assertEquals(new DAOImpl<>(Food.class).save(food), food);
        
        
        Map<Ingredient, Double> ingredients = newRecipe.getIngredients();
        ingredients.put(newIngredient, 1.5);
        final DAO<Recipe> recipeDAO = new DAOImpl<>(Recipe.class);
        newRecipe.setName("Cibule naměkko postaru" + getRandomString())
                 .setDescription("Prostě si uděláte cibuli." + getRandomString())
                 .setPeopleAmount((short) 2)
                 .setDifficulty(difficulty);
        newRecipe.setIngredients(ingredients);
        newRecipe.setFood(food);
        
        Assert.assertEquals(recipeDAO.save(newRecipe), newRecipe);
        System.out.println(recipeDAO.find(newRecipe.getIdRecipe()).getIngredients().containsKey(newIngredient));
    }
    
    @Test(priority = -1)
    public void testFind() throws Exception {
        final DAO<Recipe> recipeIDAO = new DAOImpl<>(Recipe.class);
        final Recipe newRecipe = new Recipe().setName(getRandomString()).setPeopleAmount((short) 1).setDescription(getRandomString());
        Assert.assertEquals(recipeIDAO.save(newRecipe), newRecipe);
        Assert.assertEquals(recipeIDAO.find(newRecipe.getIdRecipe()), newRecipe);
    }
    
    @Test
    public void testList() throws Exception {
        final DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
        
        //Create and save first ingredient
        final Ingredient newIngredient = new Ingredient().setAmount(2)
                                                         .setExpirationDate(new Date(16111995))
                                                         .setName(getRandomString())
                                                         .setPricePerUnit(BigDecimal.TEN)
                                                         .setUnit("testUnit");
        Assert.assertEquals(ingredientDAO.save(newIngredient), newIngredient);
        
        //Create and save second ingredient
        final Ingredient newIngredient2 = new Ingredient().setAmount(3)
                                                          .setExpirationDate(new Date(16111996))
                                                          .setName(getRandomString())
                                                          .setPricePerUnit(BigDecimal.ONE)
                                                          .setUnit("testUnit2");
        Assert.assertEquals(ingredientDAO.save(newIngredient2), newIngredient2);
        
        //Create and save difficulty
        final Difficulty newDifficulty = new Difficulty().setLevel(getRandomString());
        Assert.assertEquals(new DAOImpl<>(Difficulty.class).save(newDifficulty), newDifficulty);
        
        //Get results
        final Collection<Ingredient> result = ingredientDAO.list();
        final Collection<Ingredient> expected = Arrays.asList(newIngredient, newIngredient2);
        
        Assert.assertTrue(result.containsAll(expected) && !result.contains(newDifficulty));
    }
    
    @Test
    public void testDelete() throws Exception {
        final DAO<FoodType> foodTypeDAO = new DAOImpl<>(FoodType.class);
        
        //Create and save food type
        final FoodType newFoodType = new FoodType().setTypefood(getRandomString());
        Assert.assertEquals(foodTypeDAO.save(newFoodType), newFoodType);
        
        //Delete food type
        foodTypeDAO.delete(newFoodType.getIdFoodtype());
        Assert.assertNull(foodTypeDAO.find(newFoodType.getIdFoodtype()));
    }
    
    
    @Test
    public void testUpdate() throws Exception {
        final DAO<Difficulty> difficultyDAO = new DAOImpl<>(Difficulty.class);
        final Difficulty newDifficulty = new Difficulty().setLevel(getRandomString());
        
        // Persist difficulty
        Assert.assertEquals(difficultyDAO.save(newDifficulty), newDifficulty);
        
        // Change saved difficulty
        final String newLevel = getRandomString();
        newDifficulty.setLevel(newLevel);
        Assert.assertEquals(difficultyDAO.update(newDifficulty), newDifficulty);
        
        // Verify saved difficulty
        final Difficulty foundDifficulty = difficultyDAO.find(newDifficulty.getIdDifficulty());
        Assert.assertEquals(foundDifficulty.getIdDifficulty(), newDifficulty.getIdDifficulty());
        Assert.assertEquals(foundDifficulty.getLevel(), newLevel);
    }
    
    @Test
    public void testCookableRecipes() throws Exception {
        final DAO<Difficulty> difficultyDAO = new DAOImpl<>(Difficulty.class);
        final List<Recipe> result = difficultyDAO.listCookableRecipes();
        Assert.assertNotNull(result);
    }
}