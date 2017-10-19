package permissions.db;

import java.util.List;

public interface Repository<TEntity> {

    TEntity withId(int id);
    List<TEntity> allOnPage(PagingInfo page);
    void add(TEntity entity);
    void modify(TEntity entity);
    void remove(TEntity entity);
}
