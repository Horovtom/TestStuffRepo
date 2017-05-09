package com.cvut.fel.horovtom.logic;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.dao.DAOImpl;
import com.cvut.fel.horovtom.data.dummyDAO.*;
import com.cvut.fel.horovtom.data.model.*;
import javafx.collections.ObservableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Hermes235 on 27.4.2017.
 */
public class DatabaseOperator {
    @Nonnull public static final DatabaseOperator OPERATOR = new DatabaseOperator();
    private static final Logger LOG = Logger.getLogger(DatabaseOperator.class.getName());
    private Recipe selectedRecipe = null;
    private ArrayList<Ingredient> selectedRecipeIngredients = null;
    private int selectedRecipeIndex = -1;
    private boolean newRecipe = false;
    /*
    @Nonnull private DAO<Difficulty> difficultyDAO = new DAOImpl<>(Difficulty.class);
    @Nonnull private DAO<Food> foodDAO = new DAOImpl<>(Food.class);
    @Nonnull private DAO<FoodType> foodTypeDAO = new DAOImpl<>(FoodType.class);
    @Nonnull private DAO<Recipe> recipeDAO = new DAOImpl<>(Recipe.class);
    @Nonnull private DAO<Ingredient> ingredientDAO = new DAOImpl<>(Ingredient.class);
    */
    //TODO: JUST FOR TESTING:

    @Nonnull private DAO<Difficulty> difficultyDAO = new DummyDifficultyDAO();
    @Nonnull private DAO<Food> foodDAO = new DummyFoodDAO();
    @Nonnull private DAO<FoodType> foodTypeDAO = new DummyFoodTypeDAO();
    @Nonnull private DAO<Recipe> recipeDAO = new DummyRecipeDAO();
    @Nonnull private DAO<Ingredient> ingredientDAO = new DummyIngredientDAO();

    
    @Nullable private List<FoodType> foodTypeList = null;
    @Nullable private List<Food> foodList = null;
    @Nullable private List<Recipe> recipeList = null;
    @Nullable private List<Ingredient> ingredientList = null;
    @Nullable private List<Difficulty> difficultyList = null;

    //region Testing Data
    
    private DatabaseOperator() {
//            createIngredients();
    }

    public void setSelectedRecipeIndex(int index) {
        if (index == -1) {
            newRecipe = true;
            selectedRecipe = new Recipe();
        } else {
            newRecipe = false;
            assert recipeList != null;
            selectedRecipe = recipeList.get(index);
            selectedRecipeIndex = index;
        }
    }

    /**
     * @return if value of {@link #selectedRecipeIndex} is the last of {@link #recipeList}
     */
    public boolean isLastRecipe() {
        assert recipeList != null;
        return (selectedRecipeIndex == recipeList.size() - 1);
    }

    /**
     * @return if value of {@link #selectedRecipeIndex} is the first of {@link #recipeList}
     */
    public boolean isFirstRecipe() {
        assert recipeList != null;
        return selectedRecipeIndex == 0;
    }

    /**
     * Switches {@link #selectedRecipe} to the next
     */
    public void nextRecipe() {
        assert recipeList != null;
        selectedRecipe = recipeList.get(++selectedRecipeIndex);
    }

    /**
     * Switches {@link #selectedRecipe} to the previous
     */
    public void previousRecipe() {
        assert recipeList != null;
        selectedRecipe = recipeList.get(--selectedRecipeIndex);
    }
    
    /**
     * Fills database with testing data
     */
    private void createIngredients() {
        Ingredient brambory = new Ingredient();
        Ingredient maso = new Ingredient();
        Ingredient hrach = new Ingredient();
        brambory.setAmount(2);
        maso.setAmount(0.5);
        hrach.setAmount(0.2);
        brambory.setExpirationDate(new Date(System.currentTimeMillis()));
        maso.setExpirationDate(new Date(System.currentTimeMillis()));
        hrach.setExpirationDate(new Date(System.currentTimeMillis()));
        brambory.setName("Brambory");
        maso.setName("Maso");
        hrach.setName("Hrach");
        brambory.setPricePerUnit(BigDecimal.valueOf(30));
        maso.setPricePerUnit(BigDecimal.valueOf(120));
        hrach.setPricePerUnit(BigDecimal.valueOf(50));
        brambory.setUnit("kg");
        maso.setUnit("kg");
        hrach.setUnit("kg");
        
        ingredientDAO.save(brambory);
        ingredientDAO.save(hrach);
        ingredientDAO.save(maso);
        createFoodTypes(maso, hrach, brambory);
    }
    
    /**
     * Fills database with testing data
     */
    private void createRecipes(Food rizek, Food knedlo, Difficulty hard, Difficulty easy, Ingredient maso, Ingredient hrach, Ingredient brambory) {
        Recipe rizekPonovu = new Recipe();
        Recipe rizekPostaru = new Recipe();
        Recipe knedloRecept = new Recipe();
        rizekPonovu.setFood(rizek);
        rizekPostaru.setFood(rizek);
        knedloRecept.setFood(knedlo);
        rizekPostaru.setName("Řízek postaru");
        rizekPonovu.setName("Řízek ponovu");
        knedloRecept.setName("Knedlo vepřo normálně");
        rizekPostaru.setDescription("Uděláte ho postaru");
        rizekPonovu.setDescription("Uděláte ho ponovu");
        knedloRecept.setDescription("Normálně ho prostě uděláte");
        rizekPostaru.setDifficulty(hard);
        rizekPonovu.setDifficulty(easy);
        knedloRecept.setDifficulty(easy);
        Map<Ingredient, Double> postaru = new HashMap<>();
        postaru.put(maso, 2.0);
        postaru.put(hrach, 2.0);
        rizekPostaru.setIngredients(postaru);
        Map<Ingredient, Double> ponovu = new HashMap<>();
        ponovu.put(maso, 1.0);
        rizekPonovu.setIngredients(ponovu);
        Map<Ingredient, Double> knedloMap = new HashMap<>();
        knedloMap.put(brambory, 0.5);
        knedloRecept.setIngredients(knedloMap);
        rizekPostaru.setPeopleAmount((short) 3);
        rizekPonovu.setPeopleAmount((short) 2);
        knedloRecept.setPeopleAmount((short) 1);
        
        recipeDAO.save(rizekPonovu);
        recipeDAO.save(rizekPostaru);
        recipeDAO.save(knedloRecept);
    }
    
    /**
     * Fills database with testing data
     */
    private void createFoodTypes(Ingredient maso, Ingredient hrach, Ingredient brambory) {
        FoodType hlavni = new FoodType();
        FoodType polevka = new FoodType();
        polevka.setFoods(new ArrayList<>());
        hlavni.setTypefood("Hlavní jídlo");
        polevka.setTypefood("Polévka");
        foodTypeDAO.save(hlavni);
        foodTypeDAO.save(polevka);
        createFoods(maso, hrach, brambory, hlavni);
    }
    
    /**
     * Fills database with testing data
     */
    private void createFoods(Ingredient maso, Ingredient hrach, Ingredient brambory, FoodType hlavni) {
        Food knedlo = new Food();
        Food rizek = new Food();
        knedlo.setName("Knedlo vepřo zelo");
        rizek.setName("Vídeňský řízek");
        List<FoodType> types = new ArrayList<>();
        types.add(hlavni);
        knedlo.setFoodTypes(types);
        List<FoodType> types2 = new ArrayList<>();
        types2.add(hlavni);
        rizek.setFoodTypes(types2);
        
        foodDAO.save(rizek);
        foodDAO.save(knedlo);
        createDifficulties(maso, hrach, brambory, rizek, knedlo);
    }
    
    //endregion
    
    /**
     * Fills database with testing data
     */
    private void createDifficulties(Ingredient maso, Ingredient hrach, Ingredient brambory, Food rizek, Food knedlo) {
        Difficulty easy = new Difficulty();
        Difficulty hard = new Difficulty();
        
        easy.setLevel("Easy");
        hard.setLevel("Hard");
        difficultyDAO.save(easy);
        difficultyDAO.save(hard);
        createRecipes(rizek, knedlo, hard, easy, maso, hrach, brambory);
    }
    
    /**
     * Asks data for list of FoodTypes and saves them into {@link #foodTypeList}
     *
     * @return List of names of these foodTypes in order
     */
    @Nonnull
    public List<String> getFoodTypes() {
        List<String> output = new ArrayList<>();
        foodTypeList = foodTypeDAO.list();
        output.addAll(foodTypeList.stream().map(FoodType::getTypefood).collect(Collectors.toList()));
        return output;
    }
    
    /**
     * Asks data for list of Foods and saves them into {@link #foodList}
     *
     * @return List of names of these foods in order
     */
    @Nonnull
    public List<String> getFoods() {
        ArrayList<String> out = new ArrayList<>();
        foodList = foodDAO.list();
        out.addAll(foodList.stream().map(Food::getName).collect(Collectors.toList()));
        return out;
    }
    
    /**
     * Asks data for list of Recipes and saves them into {@link #recipeList}
     *
     * @return List of names of these recipes in order
     */
    @Nonnull
    public List<String> getRecipes() {
        ArrayList<String> out = new ArrayList<>();
        recipeList = recipeDAO.list();
        out.addAll(recipeList.stream().map(Recipe::getName).collect(Collectors.toList()));
        return out;
    }
    
    /**
     * Asks data for list of Ingredients and saves them into {@link #ingredientList}
     *
     * @return List of names of these ingredients in order
     */
    @Nonnull
    public List<String> getIngredients() {
        ingredientList = ingredientDAO.list()
                                      .stream()
                                      .filter(ingredient -> ingredient.getAmount() > 0)
                                      .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                                      .collect(Collectors.toList());
        return ingredientList.stream().map(Ingredient::getName).collect(Collectors.toList());
    }
    
    /**
     * Queries saved foodTypes in {@link #foodTypeList} on specified index and returns list of foods with that type.
     * Saves this list into {@link #foodList}
     */
    @Nonnull
    public List<String> getFoodsWithType(int typeIndex) {
        if (foodTypeList == null || typeIndex >= foodTypeList.size()) {
            LOG.warning("Trying to access foodTypeList when it is not initialized!");
            return new ArrayList<>();
        }
        List<String> out = new ArrayList<>();
        foodList = foodTypeList.get(typeIndex).getFoods();
        out.addAll(foodList.stream().map(Food::getName).collect(Collectors.toList()));
        
        return out;
    }
    
    /**
     * Queries saved foods in {@link #foodList} on specified index and returns list of recipes of that food.
     * Saves this list into {@link #recipeList}
     */
    @Nonnull
    public List<String> getRecipesOfFood(int foodIndex) {
        if (foodList == null || foodIndex >= foodList.size()) {
            LOG.warning("Trying to access foodList when it is not initialized!");
            return new ArrayList<>();
        }
        List<String> out = new ArrayList<>();
        recipeList = foodList.get(foodIndex).getRecipes();
        out.addAll(recipeList.stream().map(Recipe::getName).collect(Collectors.toList()));
        
        return out;
    }
    
    
    @Nonnull
    public List<String> getDifficulties() {
        ArrayList<String> out = new ArrayList<>();
        difficultyList = difficultyDAO.list();
        out.addAll(difficultyList.stream().map(Difficulty::getLevel).collect(Collectors.toList()));
        return out;
    }
    
    
    @Nonnull
    public List<String> getRecipesOfFoodTypes(int foodTypeIndex) {
        recipeList = recipeDAO.list();
        ArrayList<String> out = new ArrayList<>();
        ArrayList<Recipe> newRecipeList = new ArrayList<>();
        assert foodTypeList != null;
        FoodType foodType = foodTypeList.get(foodTypeIndex);
        
        for (Recipe aRecipeList : recipeList) {
            if (aRecipeList.getFood() == null) {
                LOG.severe("Found recipe which does not cook any food! Deleting it from database: " + aRecipeList.getName());
                recipeDAO.delete(aRecipeList.getIdRecipe());
                return getRecipesOfFoodTypes(foodTypeIndex);
            }
            if (aRecipeList.getFood().getFoodTypes().contains(foodType)) {
                newRecipeList.add(aRecipeList);
                out.add(aRecipeList.getName());
            }
        }
        
        recipeList = newRecipeList;

//        for (int i = recipeList.size() - 1; i >= 0; i--) {
//            if (!recipeList.get(i).getFood().getFoodTypes().contains(foodType)) {
//                recipeList.remove(recipeList.get(i));
//            }
//        }

//        out.addAll(recipeList.stream().map(Recipe::getName).collect(Collectors.toList()));
        return out;
    }
    
    public Ingredient getIngredient(int selected) {
        return ingredientList.get(selected);
    }
    
    public boolean isFirstIngredient(int selected) {
        return selected == 0;
    }
    
    public boolean isLastIngredient(int selected) {
        return ingredientList.size() - 1 == selected;
    }
    
    /**
     * Updates ingredient in the data
     *
     * @return index of ingredient in the list, -1 if error occurred...
     */
    public int editIngredient(int selected, String name, String unit, BigDecimal price, double amount, Date expiration) {
        LOG.info("Changing ingredient " + ingredientList.get(selected));
        Ingredient ingredient = ingredientList.get(selected);
        ingredient.setName(name);
        ingredient.setPricePerUnit(price);
        ingredient.setExpirationDate(expiration);
        ingredient.setUnit(unit);
        ingredient.setAmount(amount);
        Ingredient savedIngredient = ingredientDAO.update(ingredient);
        if (savedIngredient == null) {
            return -1;
        }
        int id = savedIngredient.getIdIngredient();
        
        getIngredients();
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getIdIngredient() == id) {
                return i;
            }
        }
        LOG.warning(() -> "Saved ingredient missing in the data! ID: " + id);
        return -1;
    }
    
    /**
     * Creates new Ingredient in the data
     *
     * @return index of that new ingredient in the list, -1 if error occurred...
     */
    public int addIngredient(String name, String unit, BigDecimal price, double amount, Date expiration) {
        LOG.info(() -> "Adding new ingredient " + name);
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName(name);
        newIngredient.setUnit(unit);
        newIngredient.setPricePerUnit(price);
        newIngredient.setAmount(amount);
        newIngredient.setExpirationDate(expiration);
        Ingredient savedIngredient = ingredientDAO.save(newIngredient);
        if (savedIngredient != newIngredient) {
            return -1;
        }
        int id = savedIngredient.getIdIngredient();
        getIngredients();
        assert ingredientList != null;
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getIdIngredient() == id) {
                return i;
            }
        }
        LOG.warning(() -> "Saved ingredient missing in the data! ID: " + id);
        return -1;
    }
    
    /**
     * Deletes ingredient in the data on specified index.
     * Does not refresh {@link #ingredientList}
     */
    public void deleteIngredient(int selectedIndex) {
        Stream.of(selectedIndex)
              .map(index -> {
                  assert ingredientList != null;
                  return ingredientList.get(selectedIndex).getIdIngredient();
              })
              .map(id -> ingredientDAO.find(id))
              .filter(Objects::nonNull)
              .map(ingredient -> ingredient.setAmount(0))
              .forEach(ingredient -> ingredientDAO.update(ingredient));
    }
    
    public Recipe getRecipe(int selected) {
        assert recipeList != null;
        return recipeList.get(selected);
    }

    public int getDifficultySelectionIndex() {
        assert difficultyList != null;
        if (newRecipe) return -1;
        int idDifficulty = selectedRecipe.getDifficulty().getIdDifficulty();
        for (int i = 0; i < difficultyList.size(); i++) {
            if (difficultyList.get(i).getIdDifficulty() == idDifficulty) {
                return i;
            }
        }
        LOG.warning("Haven't found Difficulty specified for " + selectedRecipe);
        return -1;
    }

    public int getFoodSelectionIndex() {
        assert foodList != null;
        if (newRecipe) return -1;
        int idFood = selectedRecipe.getFood().getIdFood();
        for (int i = 0; i < foodList.size(); i++) {
            if (foodList.get(i).getIdFood() == idFood) {
                return i;
            }
        }
        LOG.warning("Haven't found Food specified for " + selectedRecipe);
        return -1;
    }
    
    @Nonnull
    public List<String> getIngredientsInRecipe() {
        assert recipeList != null;
        selectedRecipeIngredients = new ArrayList<>();
        List<String> out = new ArrayList<>();
        Recipe recipe = selectedRecipe;
        Map<Ingredient, Double> ingredients = recipe.getIngredients();

        for (Map.Entry<Ingredient, Double> ingredientDoubleEntry : ingredients.entrySet()) {
            selectedRecipeIngredients.add(ingredientDoubleEntry.getKey());
            out.add(ingredientDoubleEntry.getKey().getName() + " - " + ingredientDoubleEntry.getValue() + " " +
                    ingredientDoubleEntry.getKey().getUnit());
        }

        return out;
    }
    
    public String getUnitOfIngredient(int selected) {
        if (selected == -1) return "";
        assert ingredientList != null;
        return ingredientList.get(selected).getUnit();
    }

    public int addRecipe(Recipe recipe) {
        boolean value = recipeDAO.save(recipe) == recipe;
        if (!value) return -1;
        assert recipeList != null;
        recipeList.add(recipe);
        return recipeList.size() - 1;
    }

    public int changeRecipe(Recipe recipe, int selected) {
        boolean value = recipeDAO.update(recipe) == recipe;
        if (!value) return -1;
        assert recipeList != null;
        recipeList.set(selected, recipe);
        return selected;
    }

    @Deprecated
    public boolean changeRecipe(int selected, String name, short ppl, String desc, int diffIndex, int foodIndex,
                                @Nonnull ObservableList<String> ingredientsNames) {
        assert recipeList != null;
        Recipe recipe = createRecipe(recipeList.get(selected), name, ppl, desc, diffIndex, foodIndex, ingredientsNames);
        
        return recipeDAO.update(recipe) == recipe;
    }
    
    /**
     * Adds new Recipe in the database
     *
     */
    @Deprecated
    public boolean addRecipe(String name, short ppl, String desc, int diffIndex, int foodIndex, @Nonnull ObservableList<String> ingredientsNames) {
        Recipe recipe = createRecipe(new Recipe(), name, ppl, desc, diffIndex, foodIndex, ingredientsNames);
        
        return recipeDAO.save(recipe) == recipe;
    }
    
    @Nonnull
    @Deprecated
    private Recipe createRecipe(@Nonnull Recipe recipe, String name, short ppl, String desc, int diffIndex, int foodIndex,
                                @Nonnull ObservableList<String> ingredientsNames) {
        assert difficultyList != null;
        assert foodList != null;
        assert ingredientList != null;
        
        recipe.setName(name);
        recipe.setPeopleAmount(ppl);
        recipe.setDescription(desc);
        recipe.setDifficulty(difficultyList.get(diffIndex));
        recipe.setFood(foodList.get(foodIndex));
        
        Map<Ingredient, Double> ingredients = new HashMap<>();
        for (String ingredient : ingredientsNames) {
            StringTokenizer st = new StringTokenizer(ingredient, " ");
            String ingName = st.nextToken();
            st.nextToken();
            Double amount = Double.valueOf(st.nextToken());
            
            for (Ingredient ingredient1 : ingredientList) {
                if (ingredient1.getName().equals(ingName)) {
                    ingredients.put(ingredient1, amount);
                    break;
                }
            }
        }
        recipe.setIngredients(ingredients);
        return recipe;
    }

    /**
     * Returns false if there is already such ingredient in recipe
     */
    public boolean addIngredientToRecipe(int selectedIndex, double amount) {
        assert ingredientList != null;

        Ingredient ing = ingredientList.get(selectedIndex);
        for (Ingredient ingredient : selectedRecipe.getIngredients().keySet()) {
            if (ingredient.getIdIngredient() == ing.getIdIngredient()) {
                LOG.info("There is already ingredient " + ing + " in " + selectedRecipe);
                return false;
            }
        }

        selectedRecipe.getIngredients().put(ing, amount);
        return true;
    }

    public Recipe getSelectedRecipe() {
        return selectedRecipe;
    }

    /**
     * Need to reload {@link #recipeList} after this!
     */
    public boolean saveRecipe() {
        if (newRecipe) {
            return recipeDAO.save(selectedRecipe) == selectedRecipe;
        } else {
            return recipeDAO.update(selectedRecipe) == selectedRecipe;
        }
    }

    public void setSelectedRecipeFields(String name, String description, short pplAmount) {
        selectedRecipe.setName(name);
        selectedRecipe.setPeopleAmount(pplAmount);
        selectedRecipe.setDescription(description);
    }

    public void setSelectedRecipeDifficulty(int selectedRecipeDifficulty) {
        assert difficultyList != null;
        if (selectedRecipeDifficulty >= 0) selectedRecipe.setDifficulty(difficultyList.get(selectedRecipeDifficulty));
    }

    public void setSelectedRecipeFood(int selectedRecipeFood) {
        assert foodList != null;
        if (selectedRecipeFood >= 0) selectedRecipe.setFood(foodList.get(selectedRecipeFood));


    }

    public void removeIngredientFromCurrRecipe(int selectedIndex) {
        assert ingredientList != null;
        assert selectedRecipeIngredients != null;
        Ingredient ingredient = selectedRecipeIngredients.get(selectedIndex);
        LOG.info("Removing " + ingredient.getName() + " from recipe " + selectedRecipe);

        selectedRecipe.getIngredients().remove(ingredient);
    }

    public boolean isNewRecipe() {
        return newRecipe;
    }
}
