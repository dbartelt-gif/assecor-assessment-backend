package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Beispiel-Implementierung eines PersonRepositorys.
 * Dient lediglich zur Demonstration, wie eine alternative
 * Datenquelle (z. B. Datenbank statt CSV) eingebunden werden kann.
 */
@Repository
@Qualifier("dbRepo")
public class DbPersonRepository implements PersonRepository {
    @Override
    public List<Person> findAll() {
        return List.of();
    }

    @Override
    public Optional<Person> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Person> findByColor(String color) {
        return List.of();
    }

    @Override
    public Person save(Person person) {
        return null;
    }
}
