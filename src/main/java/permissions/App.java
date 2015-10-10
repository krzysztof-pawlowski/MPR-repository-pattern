package permissions;

import java.util.List;

import permissions.db.PersonDbManager;
import permissions.domain.Person;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        PersonDbManager mgr = new PersonDbManager();
        
        List<Person> allPersons = mgr.getAll();
        
        for(Person p : allPersons){
        	System.out.println(p.getId()+" "+p.getName()+" "+p.getSurname());
        }
        mgr.deleteById(3);
        
        Person toUpdate = new Person();
        
        toUpdate.setName("Bolesław");
        toUpdate.setSurname("Prus");
        toUpdate.setId(2);
        
        mgr.update(toUpdate);
    }
}
