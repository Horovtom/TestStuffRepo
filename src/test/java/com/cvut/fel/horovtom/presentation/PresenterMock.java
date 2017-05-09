package com.cvut.fel.horovtom.presentation;

import com.cvut.fel.horovtom.data.model.Ingredient;
import com.cvut.fel.horovtom.data.model.Recipe;
import com.google.gson.Gson;
import javafx.scene.control.TextArea;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Random;

/**
 * @author Tomáš Hamsa on 22.04.2017.
 */
class PresenterMock implements Presenter {
    @Nonnull private final Gson jsonBuilder;
    @Nonnull private final Random random;
    private TextArea text;
    
    public PresenterMock() {
        jsonBuilder = new Gson();
        random = new Random(475621);
    }
    
    @Override
    public void showIngredients() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(jsonBuilder.toJson(getRandomIngredient())).append(System.lineSeparator());
        }
        text.setText(result.toString());
    }
    
    @Override
    public void setText(@Nonnull TextArea text) {
        this.text = text;
    }
    
    @Nonnull
    private Ingredient getRandomIngredient() {
        return new Ingredient().setAmount(random.nextInt())
                               .setExpirationDate(new Date(random.nextLong()))
                               .setName("This is a name of ingredient")
                               .setPricePerUnit(new BigDecimal(random.nextDouble()))
                               .setUnit("This is the unit of ingredient");
    }
    
    @Override
    public void showCookableRecipes() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(jsonBuilder.toJson(getRandomRecipe())).append(System.lineSeparator());
        }
        text.setText(result.toString());
    }
    
    @Nonnull
    private Recipe getRandomRecipe() {
        return new Recipe().setDescription("This is a description of recipe")
                           .setName("This is a name of recipe")
                           .setPeopleAmount((short) (random.nextInt() % Short.MAX_VALUE));
    }
}
