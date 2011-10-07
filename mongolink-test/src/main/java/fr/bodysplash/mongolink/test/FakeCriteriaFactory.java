package fr.bodysplash.mongolink.test;

import fr.bodysplash.mongolink.MongoSession;
import fr.bodysplash.mongolink.domain.criteria.*;

public class FakeCriteriaFactory extends CriteriaFactory {

    @Override
    public Criteria create(Class<?> type, MongoSession mongoSession) {
        return new FakeCriteria(type, mongoSession);
    }
}
