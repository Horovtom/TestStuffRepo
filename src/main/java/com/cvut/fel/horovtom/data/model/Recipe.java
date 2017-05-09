package com.cvut.fel.horovtom.data.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hermes235 on 15.4.2017.
 */
@Entity
public class Recipe implements Serializable {
    
    private static final long serialVersionUID = -5388641714772391562L;
    @Id
    @GeneratedValue
    private int idRecipe;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String name;
    
    @Basic
    @Column(nullable = false)
    private short peopleAmount;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDifficulty")
    private Difficulty difficulty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFood")
    private Food food;
    
    @ElementCollection
    @CollectionTable(name = "ingredient_has_recipe")
    private Map<Ingredient, Double> ingredients = new HashMap<>();
    
    public Food getFood() {
        return food;
    }
    
    @Nonnull
    public Recipe setFood(Food food) {
        this.food = food;
        return this;
    }
    
    @Nonnull
    public Map<Ingredient, Double> getIngredients() {
        return ingredients;
    }
    
    @Nonnull
    public Recipe setIngredients(Map<Ingredient, Double> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
    
    public int getIdRecipe() {
        return idRecipe;
    }
    
    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }
    
    public String getName() {
        return name;
    }
    
    @Nonnull
    public Recipe setName(String name) {
        this.name = name;
        return this;
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    @Nonnull
    public Recipe setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }
    
    public short getPeopleAmount() {
        return peopleAmount;
    }
    
    @Nonnull
    public Recipe setPeopleAmount(short peopleAmount) {
        this.peopleAmount = peopleAmount;
        return this;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Nonnull
    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }
    
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Recipe that = (Recipe) o;
        
        if (idRecipe != that.idRecipe) {
            return false;
        }
        if (peopleAmount != that.peopleAmount) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = idRecipe;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) peopleAmount;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "Recipe{" + "idRecipe=" + idRecipe + ", name='" + name + '\'' + ", peopleAmount=" + peopleAmount + ", description='" + description + "'}";
    }
}
