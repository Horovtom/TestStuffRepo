package com.cvut.fel.horovtom.data.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Hermes235 on 15.4.2017.
 */
@Entity
public class Food implements Serializable {
    
    private static final long serialVersionUID = -3455869602809729828L;
    @Id
    @GeneratedValue
    private int idFood;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "food_has_foodtype")
    private List<FoodType> foodTypes;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "food") private List<Recipe> recipes;
    
    @Nonnull
    public List<Recipe> getRecipes() {
        return recipes;
    }
    
    @Nonnull
    public Food setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }
    
    @Nonnull
    public List<FoodType> getFoodTypes() {
        return foodTypes;
    }
    
    @Nonnull
    public Food setFoodTypes(List<FoodType> foodTypes) {
        this.foodTypes = foodTypes;
        return this;
    }
    
    public int getIdFood() {
        return idFood;
    }
    
    public void setIdFood(int idFood) {
        this.idFood = idFood;
    }
    
    public String getName() {
        return name;
    }
    
    @Nonnull
    public Food setName(String name) {
        this.name = name;
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
        
        Food that = (Food) o;
        
        if (idFood != that.idFood) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = idFood;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "Food{" + "idFood=" + idFood + ", name='" + name + '\'' + ", foodTypes=" + foodTypes + ", recipes=" + recipes + '}';
    }
}
