package de.assecor.assessment.persons.service;

import de.assecor.assessment.persons.model.Person;
import de.assecor.assessment.persons.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Person> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Person> getByColor(String color) {
        return repository.findByColor(color);
    }

    @Override
    public Person create(Person person) {
        return repository.save(person);
    }
}
