package permissions.db.mapper;

import permissions.db.PagingInfo;
import permissions.domain.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonMapper extends AbstractMapper<Person> {

    private static final String COLUMNS = "id, name, surname";
    public static final String SELECT_BY_NAME_STMT = "SELECT " + COLUMNS + " FROM person WHERE name=? OFFSET ? LIMIT ?";
    public static final String SELECT_BY_SURNAME_STMT =
        "SELECT " + COLUMNS + " FROM person WHERE Surname=? OFFSET ? LIMIT ?";
    public static final String FIND_ALL_ON_PAGE_STMT =
        "SELECT " + COLUMNS + " FROM people" + " WHERE id=? OFFSET ? LIMIT ?";
    public static final String FIND_STMT = "SELECT " + COLUMNS + " FROM people" + " WHERE id=?";
    public static final String INSERT_STMT = "INSERT INTO person(name,surname) VALUES(?,?)";
    public static final String UPDATE_STMT = "UPDATE person SET (name,surname)=(?,?) WHERE id=?";
    public static final String DELETE_STMT = "DELETE FROM person WHERE id=?";

    public PersonMapper(Connection connection) {
        super(connection);
    }

    public List<Person> withSurname(String surname, PagingInfo page) {

        List<Person> result = new ArrayList<Person>();
        PreparedStatement selectBySurname;
        try {
            selectBySurname = connection.prepareStatement(SELECT_BY_SURNAME_STMT);
            selectBySurname.setString(1, surname);
            selectBySurname.setInt(2, page.getCurrentPage() * page.getSize());
            selectBySurname.setInt(3, page.getSize());
            ResultSet rs = selectBySurname.executeQuery();
            while (rs.next()) {
                Person person = doLoad(rs);
                result.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Person> withName(String name, PagingInfo page) {

        List<Person> result = new ArrayList<Person>();
        PreparedStatement selectByName;
        try {
            selectByName = connection.prepareStatement(SELECT_BY_NAME_STMT);
            selectByName.setString(1, name);
            selectByName.setInt(2, page.getCurrentPage() * page.getSize());
            selectByName.setInt(3, page.getSize());
            ResultSet rs = selectByName.executeQuery();
            while (rs.next()) {
                Person person = doLoad(rs);
                result.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected Person doLoad(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setId(rs.getInt("id"));
        return person;
    }

    @Override
    protected void parametrizeInsertStatement(PreparedStatement statement, Person person) throws SQLException {

        statement.setString(1, person.getName());
        statement.setString(2, person.getSurname());
    }

    @Override
    protected void parametrizeUpdateStatement(PreparedStatement statement, Person person) throws SQLException {
        parametrizeInsertStatement(statement, person);
        statement.setLong(3, person.getId());
    }

    @Override
    protected String findStatement() {
        return FIND_STMT;
    }

    @Override
    protected String findAllOnPageStatement() {
        return FIND_ALL_ON_PAGE_STMT;
    }

    @Override
    protected String insertStatement() {
        return INSERT_STMT;
    }

    @Override
    protected String updateStatement() {
        return UPDATE_STMT;
    }

    @Override
    protected String removeStatement() {
        return DELETE_STMT;
    }
}
