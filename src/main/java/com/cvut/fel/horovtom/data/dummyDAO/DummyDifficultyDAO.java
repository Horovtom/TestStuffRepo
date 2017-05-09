package com.cvut.fel.horovtom.data.dummyDAO;

import com.cvut.fel.horovtom.data.dao.DAO;
import com.cvut.fel.horovtom.data.model.Difficulty;
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
public class DummyDifficultyDAO implements DAO<Difficulty> {
    private static final Logger LOG = Logger.getLogger(DummyDifficultyDAO.class.getName());
    
    @Nonnull static Difficulty easy = new Difficulty();
    @Nonnull static Difficulty hard = new Difficulty();
    @Nonnull private static ArrayList<Difficulty> difficulties = new ArrayList<>();
    
    public DummyDifficultyDAO() {
        easy.setLevel("Easy");
        hard.setLevel("Hard");
        easy.setIdDifficulty(1);
        hard.setIdDifficulty(2);
        List<Recipe> list = new ArrayList<>();
        list.add(DummyRecipeDAO.knedloRecept);
        list.add(DummyRecipeDAO.rizekPonovu);
        easy.setRecipes(list);
        List<Recipe> list2 = new ArrayList<>();
        list.add(DummyRecipeDAO.rizekPostaru);
        hard.setRecipes(list2);
        difficulties.add(easy);
        difficulties.add(hard);
    }
    
    @Nonnull
    @Override
    public List<Difficulty> list() {
        return difficulties;
    }
    
    @Nonnull
    @Override
    public Difficulty save(@Nonnull Difficulty entity) {
        LOG.info("Saving Difficulty: " + entity);
        difficulties.add(entity);
        return entity;
    }
    
    @Override
    public void delete(@Nonnull Serializable id) {
        LOG.info("Not-implemented! Deleting Difficulty: " + id);
        
    }
    
    @Nullable
    @Override
    public Difficulty find(@Nonnull Serializable id) {
        for (Difficulty difficulty : difficulties) {
            if (difficulty.getIdDifficulty() == (int) id) {
                return difficulty;
            }
        }
        return null;
    }
    
    @Nonnull
    @Override
    public Difficulty update(@Nonnull Difficulty entity) {
        LOG.info("Updating Difficulty: " + entity);
        for (Difficulty difficulty : difficulties) {
            if (difficulty.getIdDifficulty() == entity.getIdDifficulty()) {
                return difficulty;
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
