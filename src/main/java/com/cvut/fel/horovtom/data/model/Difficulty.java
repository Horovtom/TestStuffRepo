package com.cvut.fel.horovtom.data.model;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * n
 * Created by Hermes235 on 15.4.2017.
 */
@Entity
public class Difficulty implements Serializable {
    private static final long serialVersionUID = -263749473075611519L;
    @Id
    @GeneratedValue
    private int idDifficulty;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String level;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "difficulty") private List<Recipe> recipes;
    
    public int getIdDifficulty() {
        return idDifficulty;
    }
    
    public void setIdDifficulty(int idDifficulty) {
        this.idDifficulty = idDifficulty;
    }
    
    @Nonnull
    public List<Recipe> getRecipes() {
        return recipes;
    }
    
    @Nonnull
    public Difficulty setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }
    
    public String getLevel() {
        return level;
    }
    
    @Nonnull
    public Difficulty setLevel(@Nonnull String level) {
        this.level = level;
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
        
        Difficulty that = (Difficulty) o;
        
        if (idDifficulty != that.idDifficulty) {
            return false;
        }
        if (level != null ? !level.equals(that.level) : that.level != null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = idDifficulty;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "Difficulty{" + "idDifficulty=" + idDifficulty + ", level='" + level + '\'' + ", recipes=" + recipes + '}';
    }
}
