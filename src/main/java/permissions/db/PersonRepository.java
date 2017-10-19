package permissions.db;

import java.util.List;

import permissions.domain.Person;

public interface PersonRepository extends Repository<Person> {
    
    List<Person> withSurname(String surname, PagingInfo page);
    List<Person> withName(String name, PagingInfo page);
}
