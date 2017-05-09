package com.cvut.fel.horovtom.data.dao;

import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

/**
 * Provides basic interaction with data using {@link javax.persistence.EntityManager}
 *
 * @author Tomáš Hamsa on 21.04.2017.
 */
public interface DAO<T> {
    /**
     * Provides all model of type {@code T} from data
     *
     * @return All model with type {@code T}
     */
    @Nonnull
    List<T> list();
    
    /**
     * Saves the entity to data
     *
     * @param entity
     *         entity to be saved to data
     * @return entity as it's saved in data
     */
    @Nonnull
    T save(@Nonnull final T entity);
    
    /**
     * Deletes entity with matching id and type from data
     *
     * @param id
     *         id of entity of type {@code T} to be deleted from data
     */
    void delete(@Nonnull final Serializable id);
    
    /**
     * Searches data for entity with type {@code T} and matching id
     *
     * @param id
     *         entity which will be searched
     * @return found entity
     */
    @Nullable
    T find(@Nonnull final Serializable id);
    
    /**
     * Updates entity saved in data to match the new provided entity
     *
     * @param entity
     *         Entity which will replace the previously persisted entity
     * @return new entity as saved in data
     */
    @Nonnull
    T update(@Nonnull final T entity);
    
    @Nonnull
    List<Recipe> listCookableRecipes();
}
