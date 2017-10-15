package permissions.db.mapper;

import permissions.domain.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper extends AbstractMapper<Person> {

    private static final String COLUMNS = "id, name, surname";

    public PersonMapper(Connection connection) {
        super(connection);
    }

    @Override
    protected Person doLoad(Long id, ResultSet rs) throws SQLException {
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

}
