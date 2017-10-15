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

    public PersonMapper(Connection connection) {
        super(connection);
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
        return "SELECT " + COLUMNS + " FROM people" + " WHERE id=?";
    }

    @Override
    protected String findAllOnPageStatement() {
        return "SELECT " + COLUMNS + " FROM people" + " WHERE id=? OFFSET ? LIMIT ?";
    }

    @Override
    protected String insertStatement() {
        return "INSERT INTO person(name,surname) VALUES(?,?)";
    }

    @Override
    protected String updateStatement() {
        return "UPDATE person SET (name,surname)=(?,?) WHERE id=?";
    }

    @Override
    protected String removeStatement() {
        return "DELETE FROM person WHERE id=?";
    }

    public List<Person> withSurname(String surname, PagingInfo page) {

        List<Person> result = new ArrayList<Person>();
        PreparedStatement selectBySurname;
        try {
            selectBySurname = connection.prepareStatement(selectBySurnameStatement());
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

    private String selectBySurnameStatement() {
        return "SELECT " + COLUMNS + " FROM person WHERE Surname=? OFFSET ? LIMIT ?";
    }

    private String selectByNameStatement() {
        return "SELECT " + COLUMNS + " FROM person WHERE name=? OFFSET ? LIMIT ?";
    }

    public List<Person> withName(String name, PagingInfo page) {

        List<Person> result = new ArrayList<Person>();
        PreparedStatement selectByName;
        try {
            selectByName = connection.prepareStatement(selectByNameStatement());
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

}
