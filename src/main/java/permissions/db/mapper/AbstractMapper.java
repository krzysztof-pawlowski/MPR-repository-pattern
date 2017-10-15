package permissions.db.mapper;

import permissions.db.PagingInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T> {

    private Map<Long, T> loadedMap = new HashMap<>();
    protected Connection connection;

    abstract protected String findStatement();
    abstract protected String findAllOnPageStatement();
    abstract protected String insertStatement();
    abstract protected String updateStatement();
    abstract protected String removeStatement();

    abstract protected T doLoad(ResultSet rs) throws SQLException;
    abstract protected void parametrizeInsertStatement(PreparedStatement statement, T entity) throws SQLException;
    abstract protected void parametrizeUpdateStatement(PreparedStatement statement, T entity) throws SQLException;

    protected AbstractMapper(Connection connection) {
        this.connection = connection;
    }

    public T find(Long id) {
        T result = loadedMap.get(id);
        if (result != null) {
            return result;
        }
        PreparedStatement findStatement = null;
        try {
            findStatement = connection.prepareStatement(findStatement());
            findStatement.setLong(1, id.longValue());
            ResultSet rs = findStatement.executeQuery();
            rs.next();
            result = load(rs);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findAllOnPage(PagingInfo page) {
        List<T> results = new ArrayList<>();
        PreparedStatement findAllOnPageStatement = null;
        try {
            findAllOnPageStatement = connection.prepareStatement(findAllOnPageStatement());
            findAllOnPageStatement.setInt(1, page.getCurrentPage() * page.getSize());
            findAllOnPageStatement.setInt(2, page.getSize());
            ResultSet rs = findAllOnPageStatement.executeQuery();

            while (rs.next()) {
                T result = load(rs);
                results.add(result);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(T entity) {
        PreparedStatement addStatement = null;
        try {
            addStatement = connection.prepareStatement(insertStatement());

            parametrizeInsertStatement(addStatement, entity);

            addStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        PreparedStatement updateStatement = null;
        try {
            updateStatement = connection.prepareStatement(updateStatement());

            parametrizeUpdateStatement(updateStatement, entity);

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(Long id) {
        PreparedStatement removeStatement = null;
        try {
            removeStatement = connection.prepareStatement(removeStatement());

            removeStatement.setLong(1, id);

            removeStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private T load(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        if (loadedMap.containsKey(id)) {
            return loadedMap.get(id);
        }
        T result = doLoad(rs);
        loadedMap.put(id, result);
        return result;
    }
}
