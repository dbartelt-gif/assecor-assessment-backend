package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    List<Person> findAll();

    Optional<Person> findById(int id);

    List<Person> findByColor(String color);

    Person save(Person person);
}