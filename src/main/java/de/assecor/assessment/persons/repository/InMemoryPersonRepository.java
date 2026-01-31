package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Qualifier("memRepo")
public class InMemoryPersonRepository implements PersonRepository {

    private final ConcurrentHashMap<Integer, Person> store = new ConcurrentHashMap<>();
    private final AtomicInteger idSeq = new AtomicInteger(1);

    public void setInitialId(int nextId) {
        idSeq.set(nextId);
    }

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Person> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Person> findByColor(String color) {
        return store.values().stream()
                .filter(p -> p.color() != null && p.color().equalsIgnoreCase(color))
                .toList();
    }

    @Override
    public Person save(Person person) {
        int id = person.id() > 0 ? person.id() : idSeq.getAndIncrement();

        Person stored = new Person(
                id,
                person.name(),
                person.lastname(),
                person.city(),
                person.color()
        );

        store.put(id, stored);
        return stored;
    }
}
