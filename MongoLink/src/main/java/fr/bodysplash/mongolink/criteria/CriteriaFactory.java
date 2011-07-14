package fr.bodysplash.mongolink.criteria;

import fr.bodysplash.mongolink.MongoSession;

public class CriteriaFactory {

    public Criteria create(Class<?> type, MongoSession mongoSession) {
        return new Criteria(type, mongoSession);
    }
}