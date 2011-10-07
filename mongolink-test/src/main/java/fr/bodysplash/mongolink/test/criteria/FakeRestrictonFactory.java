package fr.bodysplash.mongolink.test.criteria;

import fr.bodysplash.mongolink.domain.criteria.*;

public class FakeRestrictonFactory extends RestrictionFactory {

    @Override
    public Restriction getEq(String field, Object value) {
        return new FakeRestrictionEq(field, value);
    }

    @Override
    public RestrictionBetween getBetween(String field, Object start, Object end) {
        return new FakeRestrictionBetween(field, start, end);
    }
}
