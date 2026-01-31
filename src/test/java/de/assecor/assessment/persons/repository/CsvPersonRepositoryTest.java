package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvPersonRepositoryTest {

    @Test
    void findAll_loadsPersons_fromCsv_andAssignsSequentialIds() {
        CsvPersonRepository repo = new CsvPersonRepository();

        List<Person> all = repo.findAll();

        assertNotNull(all);
        assertFalse(all.isEmpty());

        assertEquals(10, all.size());
        assertEquals(1, all.get(0).id());
        assertEquals(2, all.get(1).id());
        assertEquals(3, all.get(2).id());
        assertEquals("Hans", all.get(0).name());
        assertEquals("Müller", all.get(0).lastname());
        assertEquals("67742 Lauterecken", all.getFirst().city());
        assertEquals("blau", all.getFirst().color());
    }

    @Test
    void findById_returnsPerson_whenExists() {
        CsvPersonRepository repo = new CsvPersonRepository();

        Person p = repo.findById(2).orElseThrow();

        assertEquals(2, p.id());
        assertEquals("Peter", p.name());
        assertEquals("Petersen", p.lastname());
        assertEquals("18439 Stralsund", p.city());
        assertEquals("grün", p.color());
    }

    @Test
    void findById_returnsEmpty_whenMissing() {
        CsvPersonRepository repo = new CsvPersonRepository();

        assertTrue(repo.findById(999).isEmpty());
    }

    @Test
    void findByColor_filtersCaseInsensitive() {
        CsvPersonRepository repo = new CsvPersonRepository();

        List<Person> blues = repo.findByColor("BLAU");
        assertEquals(2, blues.size());

        assertTrue(blues.stream().allMatch(p -> p.color().equalsIgnoreCase("blau")));
    }

    @Test
    void save_throwsUnsupportedOperationException() {
        CsvPersonRepository repo = new CsvPersonRepository();

        assertThrows(UnsupportedOperationException.class, () ->
                repo.save(new Person(0, "X", "Y", "Z", "rot"))
        );
    }
}
