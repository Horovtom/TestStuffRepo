package com.cvut.fel.horovtom.data.dao;

import com.cvut.fel.horovtom.data.model.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Generic Data Access Object that provides operations for {@code T} type entity on data.
 *
 * @author hamsatom
 */
public class DAOImpl<T> implements DAO<T> {
    private static final Logger LOG = Logger.getLogger(DAOImpl.class.getName());
    private static final EntityManager ENTITY_MANAGER = Persistence.createEntityManagerFactory("DB-lednice").createEntityManager();
    private static final EntityTransaction TRANSACTION = ENTITY_MANAGER.getTransaction();
    private static final String PROCEDURE_NAME = "getCookableRecipes";
    private Class<T> type;
    
    public DAOImpl(@Nonnull final Class<T> typeClass) {
        type = typeClass;
    }
    
    @Override
    @Nonnull
    public List<T> list() {
        LOG.info(() -> "Listing " + type.getName());
        TRANSACTION.begin();
        final CriteriaBuilder cb = ENTITY_MANAGER.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(type);
        final Root<T> rootEntry = cq.from(type);
        final CriteriaQuery<T> all = cq.select(rootEntry);
        final TypedQuery<T> allQuery = ENTITY_MANAGER.createQuery(all);
        final List<T> allEntities = allQuery.getResultList();
        TRANSACTION.commit();
        return allEntities;
    }
    
    @Nonnull
    @Override
    public List<Recipe> listCookableRecipes() {
        TRANSACTION.begin();
        final StoredProcedureQuery storedProcedure = ENTITY_MANAGER.createStoredProcedureQuery(PROCEDURE_NAME);
        storedProcedure.execute();
        final List<Recipe> cookableRecipe = (List<Recipe>) storedProcedure.getResultList();
        TRANSACTION.commit();
        return cookableRecipe;
    }
    
    @Nonnull
    @Override
    public T save(@Nonnull  final T entity) {
        LOG.info(() -> "Registering " + entity.toString());
        TRANSACTION.begin();
        ENTITY_MANAGER.persist(entity);
        TRANSACTION.commit();
        return entity;
    }
    
    @Override
    public void delete(@Nonnull final Serializable id) {
        LOG.info(() -> "Deleting " + id.toString());
        TRANSACTION.begin();
        ENTITY_MANAGER.remove(ENTITY_MANAGER.getReference(type, id));
        TRANSACTION.commit();
    }
    
    @Override
    @Nullable
    public T find(@Nonnull final Serializable id) {
        LOG.info(() -> "Looking for " + id.toString());
        TRANSACTION.begin();
        final T foundEntity = ENTITY_MANAGER.find(type, id);
        TRANSACTION.commit();
        return foundEntity;
    }
    
    @Nonnull
    @Override
    public T update(@Nonnull final T entity) {
        LOG.info(() -> "Updating " + entity.toString());
        TRANSACTION.begin();
        ENTITY_MANAGER.merge(entity);
        TRANSACTION.commit();
        return entity;
    }
}
