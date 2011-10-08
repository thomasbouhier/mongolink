package fr.bodysplash.mongolink.domain.criteria;

import com.mongodb.DBObject;
import fr.bodysplash.mongolink.domain.CursorParameter;
import fr.bodysplash.mongolink.domain.QueryExecutor;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestsCriteria {

    @Test
    public void canTestEquality() {
        final Criteria criteria = new Criteria(mock(QueryExecutor.class));
        criteria.add(Restrictions.eq("id", "test"));

        DBObject query = criteria.createQuery();

        assertThat(query, notNullValue());
        assertThat(query.get("id"), is((Object) "test"));
    }

    @Test
    public void canTestMultipleEquality() {
        final Criteria criteria = new Criteria(mock(QueryExecutor.class));
        criteria.add(Restrictions.eq("id", "test"));
        criteria.add(Restrictions.eq("toto", "tata"));

        DBObject query = criteria.createQuery();

        assertThat(query, notNullValue());
        assertThat(query.get("id"), is((Object) "test"));
        assertThat(query.get("toto"), is((Object) "tata"));
    }

    @Test
    public void canTestBetween() {
        final Criteria criteria = new Criteria(mock(QueryExecutor.class));
        criteria.add(Restrictions.between("date", 1, 2));

        DBObject query = criteria.createQuery();

        DBObject restriction = (DBObject) query.get("date");
        assertThat(restriction, notNullValue());
        assertThat(restriction.get("$gte"), is((Object) 1));
        assertThat(restriction.get("$lt"), is((Object) 2));
    }

    @Test
    public void canUseConverterInEq() {
        final Criteria criteria = new Criteria(mock(QueryExecutor.class));
        final DateTime date = new DateTime();
        criteria.add(Restrictions.eq("date", date));

        DBObject query = criteria.createQuery();

        assertThat(query, notNullValue());
        assertThat(query.get("date"), is((Object) date.getMillis()));
    }

    @Test
    public void canUseConverterInBetween() {
        final Criteria criteria = new Criteria(mock(QueryExecutor.class));
        final DateTime date = new DateTime();
        criteria.add(Restrictions.between("date", date, date));

        DBObject query = criteria.createQuery();

        DBObject restriction = (DBObject) query.get("date");
        assertThat(restriction, notNullValue());
        assertThat(restriction.get("$gte"), is((Object) date.getMillis()));
        assertThat(restriction.get("$lt"), is((Object) date.getMillis()));
    }

    @Test
    public void canGiveLimitAndSkip() {
        final QueryExecutor executor = mock(QueryExecutor.class);
        final Criteria criteria = new Criteria(executor);
        criteria.limit(10);
        criteria.skip(3);

        criteria.list();

        final ArgumentCaptor<CursorParameter> captor = ArgumentCaptor.forClass(CursorParameter.class);
        verify(executor).execute(any(DBObject.class), captor.capture());
        final CursorParameter cursorParameter = captor.getValue();
        assertThat(cursorParameter.getLimit(), is(10));
        assertThat(cursorParameter.getSkip(), is(3));
    }
}
