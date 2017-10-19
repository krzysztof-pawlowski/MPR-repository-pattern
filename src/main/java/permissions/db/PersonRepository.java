package permissions.db;

import permissions.domain.Person;

import java.util.List;

public interface PersonRepository extends Repository<Person> {
    
    List<Person> withSurname(String surname, PagingInfo page);
    List<Person> withName(String name, PagingInfo page);
}
