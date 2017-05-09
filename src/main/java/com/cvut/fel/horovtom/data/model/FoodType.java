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
public class FoodType implements Serializable {
    private static final long serialVersionUID = 37313619759604567L;
    @Id
    @GeneratedValue
    private int idFoodtype;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String typefood;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "foodTypes") private List<Food> foods;
    
    public List<Food> getFoods() {
        return foods;
    }
    
    @Nonnull
    public FoodType setFoods(List<Food> foods) {
        this.foods = foods;
        return this;
    }
    
    public int getIdFoodtype() {
        return idFoodtype;
    }
    
    public void setIdFoodtype(int idFoodtype) {
        this.idFoodtype = idFoodtype;
    }
    
    public String getTypefood() {
        return typefood;
    }
    
    @Nonnull
    public FoodType setTypefood(String typefood) {
        this.typefood = typefood;
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
        
        FoodType that = (FoodType) o;
        
        if (idFoodtype != that.idFoodtype) {
            return false;
        }
        if (typefood != null ? !typefood.equals(that.typefood) : that.typefood != null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = idFoodtype;
        result = 31 * result + (typefood != null ? typefood.hashCode() : 0);
        return result;
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "FoodType{" + "idFoodtype=" + idFoodtype + ", typefood='" + typefood + '\'' + ", foods=" + foods + '}';
    }
}
