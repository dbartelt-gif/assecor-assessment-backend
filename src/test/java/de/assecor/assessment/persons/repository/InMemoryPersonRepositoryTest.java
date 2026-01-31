package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryPersonRepositoryTest {

    private InMemoryPersonRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryPersonRepository();
    }

    @Test
    void save_generatesId_whenIdIsZero() {
        repo.setInitialId(100);

        Person input = new Person(0, "Mia", "Mustermann", "Berlin", "rot");
        Person saved = repo.save(input);

        assertEquals(100, saved.id());
        assertEquals("Mia", saved.name());
        assertEquals("Mustermann", saved.lastname());
        assertEquals("Berlin", saved.city());
        assertEquals("rot", saved.color());

        // wirklich im Store?
        assertTrue(repo.findById(100).isPresent());
        assertEquals(saved, repo.findById(100).get());
    }

    @Test
    void save_incrementsIdSequence_forMultipleSaves() {
        repo.setInitialId(1);

        Person a = repo.save(new Person(0, "A", "AA", "X", "blau"));
        Person b = repo.save(new Person(0, "B", "BB", "Y", "grün"));
        Person c = repo.save(new Person(0, "C", "CC", "Z", "rot"));

        assertEquals(1, a.id());
        assertEquals(2, b.id());
        assertEquals(3, c.id());
    }

    @Test
    void save_keepsProvidedId_whenIdIsPositive() {
        repo.setInitialId(50);

        Person input = new Person(7, "Hans", "Müller", "Lauterecken", "blau");
        Person saved = repo.save(input);

        assertEquals(7, saved.id());

        assertEquals(saved, repo.findById(7).orElseThrow());
    }

    @Test
    void findById_returnsEmpty_whenMissing() {
        assertTrue(repo.findById(999).isEmpty());
    }

    @Test
    void findAll_returnsAllStoredPersons() {
        repo.setInitialId(10);

        repo.save(new Person(0, "A", "AA", "X", "blau")); // id 10
        repo.save(new Person(0, "B", "BB", "Y", "grün")); // id 11

        List<Person> all = repo.findAll();
        assertEquals(2, all.size());

        // Reihenfolge ist bei ConcurrentHashMap nicht garantiert -> sortieren für stabile Tests
        all = all.stream().sorted(Comparator.comparingInt(Person::id)).toList();

        assertEquals(10, all.get(0).id());
        assertEquals(11, all.get(1).id());
    }

    @Test
    void findByColor_isCaseInsensitive_andSkipsNullColors() {
        repo.setInitialId(1);

        repo.save(new Person(0, "Hans", "Müller", "Lauterecken", "blau"));
        repo.save(new Person(0, "Peter", "Petersen", "Stralsund", "BlAu")); // andere Groß/Kleinschreibung
        repo.save(new Person(0, "Null", "Color", "Nowhere", null));
        repo.save(new Person(0, "Green", "Guy", "Somewhere", "grün"));

        List<Person> blues = repo.findByColor("BLAU");
        assertEquals(2, blues.size());

        // check: alle sind blau (case-insensitive)
        assertTrue(blues.stream().allMatch(p -> p.color() != null && p.color().equalsIgnoreCase("blau")));
    }

    @Test
    void setInitialId_affectsNextGeneratedId() {
        repo.setInitialId(500);

        Person saved = repo.save(new Person(0, "X", "Y", "Z", "weiß"));
        assertEquals(500, saved.id());

        Person saved2 = repo.save(new Person(0, "X2", "Y2", "Z2", "weiß"));
        assertEquals(501, saved2.id());
    }
}
