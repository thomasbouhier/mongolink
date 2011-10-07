package fr.bodysplash.mongolink.domain.mapper;

import com.google.common.collect.Lists;
import com.mongodb.*;
import net.sf.cglib.core.ReflectUtils;
import org.apache.log4j.Logger;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class Mapper<T> {

    public Mapper(Class<T> persistentType) {
        this.persistentType = persistentType;
    }

    public Class<T> getPersistentType() {
        return persistentType;
    }

    void setContext(MapperContext context) {
        this.context = context;
    }

    void addCollection(CollectionMapper collection) {
        collection.setMapper(this);
        collections.add(collection);
    }

    public void addProperty(PropertyMapper property) {
        property.setMapper(this);
        properties.add(property);
    }

    public T toInstance(DBObject from) {
        T instance = makeInstance();
        populate(from, instance);
        return instance;
    }

    protected T makeInstance() {
        return (T) ReflectUtils.newInstance(persistentType);
    }

    final void populate(DBObject from, Object instance) {
        populateProperties(instance, from);
        populateCollections(instance, from);
        doPopulate((T) instance, from);
    }

    private void populateProperties(Object instance, DBObject from) {
        try {
            for (PropertyMapper property : properties) {
                property.populateFrom(instance, from);
            }
        } catch (Exception e) {
            LOGGER.error("Can't populateFrom properties", e);
        }
    }

    private void populateCollections(Object instance, DBObject from) {
        for (CollectionMapper collection : collections) {
            collection.populateFrom(instance, from);
        }
    }

    protected abstract void doPopulate(T instance, DBObject from);

    public DBObject toDBObject(Object element) {
        BasicDBObject object = new BasicDBObject();
        save(element, object);
        return object;
    }

    final void save(Object element, BasicDBObject object) {
        saveProperties(element, object);
        saveCollections(element, object);
        doSave(element, object);
    }

    private void saveCollections(Object element, BasicDBObject object) {
        for (CollectionMapper collection : collections) {
            collection.saveInto(element, object);
        }
    }

    private void saveProperties(Object element, BasicDBObject object) {
        for (PropertyMapper propertyMapper : properties) {
            propertyMapper.saveTo(element, object);
        }

    }

    protected abstract void doSave(Object element, BasicDBObject object);

    public MapperContext getContext() {
        return context;
    }

    public boolean canMap(Class<?> aClass) {
        return persistentType.isAssignableFrom(aClass);
    }

    public boolean isCapped() {
        return capped;
    }

    public void setCapped(boolean capped, int cappedSize, int cappedMax) {
        this.capped = capped;
        this.cappedSize = cappedSize;
        this.cappedMax = cappedMax;
    }

    public int getCappedSize() {
        return cappedSize;
    }

    public int getCappedMax() {
        return cappedMax;
    }

    private int cappedSize;
    private int cappedMax;
    private boolean capped = false;
    private static final Logger LOGGER = Logger.getLogger(EntityMapper.class);
    protected final Class<T> persistentType;
    private final List<PropertyMapper> properties = Lists.newArrayList();
    private final List<CollectionMapper> collections = Lists.newArrayList();
    private MapperContext context;
}
