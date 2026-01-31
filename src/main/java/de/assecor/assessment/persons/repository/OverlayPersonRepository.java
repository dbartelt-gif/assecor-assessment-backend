package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

import java.util.*;

@Repository
@Primary
public class OverlayPersonRepository implements PersonRepository {

    private final PersonRepository csvRepo;
    private final InMemoryPersonRepository memRepo;

    public OverlayPersonRepository(
            @Qualifier("csvRepo") PersonRepository csvRepo,
            @Qualifier("memRepo") InMemoryPersonRepository memRepo
    ) {
        this.csvRepo = csvRepo;
        this.memRepo = memRepo;
    }

    @PostConstruct
    void initIdSequence() {
        int nextId = csvRepo.findAll().stream()
                .map(Person::id)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        memRepo.setInitialId(nextId);
    }

    @Override
    public List<Person> findAll() {
        return merge(csvRepo.findAll(), memRepo.findAll());
    }

    @Override
    public Optional<Person> findById(int id) {
        return memRepo.findById(id).or(() -> csvRepo.findById(id));
    }

    @Override
    public List<Person> findByColor(String color) {
        return merge(csvRepo.findByColor(color), memRepo.findByColor(color));
    }

    @Override
    public Person save(Person person) {
        return memRepo.save(person);
    }

    private List<Person> merge(List<Person> csv, List<Person> mem) {
        Map<Integer, Person> merged = new LinkedHashMap<>();

        for (Person p : csv) {
            merged.put(p.id(), p);
        }

        for (Person p : mem) {
            merged.put(p.id(), p);
        }

        return new ArrayList<>(merged.values());
    }
}
