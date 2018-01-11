package awt.user.database;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;

public class CloseableEntityManager implements EntityManager, AutoCloseable {

    private final EntityManager mgr;

    public CloseableEntityManager(final EntityManager mgr) {
	this.mgr = mgr;
    }

    @Override
    public void persist(final Object entity) {
	this.mgr.persist(entity);
    }

    @Override
    public <T> T merge(final T entity) {
	return this.mgr.merge(entity);
    }

    @Override
    public void remove(final Object entity) {
	this.mgr.remove(entity);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
	return this.mgr.find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final Map<String, Object> properties) {
	return this.mgr.find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode) {
	return this.mgr.find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode,
	    final Map<String, Object> properties) {
	return this.mgr.find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public <T> T getReference(final Class<T> entityClass, final Object primaryKey) {
	return this.mgr.getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
	this.mgr.flush();
    }

    @Override
    public void setFlushMode(final FlushModeType flushMode) {
	this.mgr.setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
	return this.mgr.getFlushMode();
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode) {
	this.mgr.lock(entity, lockMode);
    }

    @Override
    public void lock(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
	this.mgr.lock(entity, lockMode, properties);
    }

    @Override
    public void refresh(final Object entity) {
	this.mgr.refresh(entity);
    }

    @Override
    public void refresh(final Object entity, final Map<String, Object> properties) {
	this.mgr.refresh(entity, properties);
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode) {
	this.mgr.refresh(entity, lockMode);
    }

    @Override
    public void refresh(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
	this.mgr.refresh(entity, lockMode, properties);
    }

    @Override
    public void clear() {
	this.mgr.clear();
    }

    @Override
    public void detach(final Object entity) {
	this.mgr.detach(entity);
    }

    @Override
    public boolean contains(final Object entity) {
	return this.mgr.contains(entity);
    }

    @Override
    public LockModeType getLockMode(final Object entity) {
	return this.mgr.getLockMode(entity);
    }

    @Override
    public void setProperty(final String propertyName, final Object value) {
	this.mgr.setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
	return this.mgr.getProperties();
    }

    @Override
    public Query createQuery(final String qlString) {
	return this.mgr.createQuery(qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
	return this.mgr.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(@SuppressWarnings("rawtypes") final CriteriaUpdate updateQuery) {
	return this.mgr.createQuery(updateQuery);
    }

    @Override
    public Query createQuery(@SuppressWarnings("rawtypes") final CriteriaDelete deleteQuery) {
	return this.mgr.createQuery(deleteQuery);
    }

    @Override
    public <T> TypedQuery<T> createQuery(final String qlString, final Class<T> resultClass) {
	return this.mgr.createQuery(qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(final String name) {
	return this.mgr.createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(final String name, final Class<T> resultClass) {
	return this.mgr.createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(final String sqlString) {
	return this.mgr.createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(final String sqlString, @SuppressWarnings("rawtypes") final Class resultClass) {
	return this.mgr.createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(final String sqlString, final String resultSetMapping) {
	return this.mgr.createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(final String name) {
	return this.mgr.createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName) {
	return this.mgr.createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName,
	    @SuppressWarnings("rawtypes") final Class... resultClasses) {
	return this.mgr.createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(final String procedureName,
	    final String... resultSetMappings) {
	return this.mgr.createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public void joinTransaction() {
	this.mgr.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
	return this.mgr.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(final Class<T> cls) {
	return this.mgr.unwrap(cls);
    }

    @Override
    public Object getDelegate() {
	return this.mgr.getDelegate();
    }

    @Override
    public void close() {
	this.mgr.close();
    }

    @Override
    public boolean isOpen() {
	return this.mgr.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
	return this.mgr.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
	return this.mgr.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
	return this.mgr.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
	return this.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(final Class<T> rootType) {
	return this.mgr.createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(final String graphName) {
	return this.mgr.createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(final String graphName) {
	return this.mgr.getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(final Class<T> entityClass) {
	return this.mgr.getEntityGraphs(entityClass);
    }
}
