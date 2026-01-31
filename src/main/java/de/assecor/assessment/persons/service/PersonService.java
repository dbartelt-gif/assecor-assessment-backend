package de.assecor.assessment.persons.service;

import de.assecor.assessment.persons.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> getAll();

    Optional<Person> getById(int id);

    List<Person> getByColor(String color);

    Person create(Person person);
}