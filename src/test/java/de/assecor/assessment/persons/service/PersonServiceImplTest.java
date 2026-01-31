package de.assecor.assessment.persons.service;

import de.assecor.assessment.persons.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonServiceIntegrationTest {

    @Autowired
    private PersonService service;

    @Test
    void contextLoads_andServiceIsWired() {
        assertNotNull(service);
    }

    @Test
    void getAll_returnsCsvPersons() {
        var all = service.getAll();

        assertNotNull(all);
        assertFalse(all.isEmpty(), "CSV sollte mindestens einen Datensatz liefern");
    }

    @Test
    void create_persistsInOverlay_andIsReadableAfterwards() {
        int before = service.getAll().size();

        Person created = service.create(new Person(
                0,
                "Integration",
                "Test",
                "Berlin",
                "rot"
        ));

        assertTrue(created.id() > 0, "ID sollte vergeben werden");
        assertEquals("Integration", created.name());

        Person fetched = service.getById(created.id()).orElseThrow();
        assertEquals(created.id(), fetched.id());
        assertEquals("Berlin", fetched.city());
        assertEquals("rot", fetched.color());

        int after = service.getAll().size();
        assertEquals(before + 1, after, "Nach create() muss getAll() einen Eintrag mehr liefern");
    }

    @Test
    void getByColor_includesCreatedPerson() {
        Person created = service.create(new Person(
                0,
                "Color",
                "Filter",
                "Hamburg",
                "blau"
        ));

        var blues = service.getByColor("blau");

        assertTrue(
                blues.stream().anyMatch(p -> p.id() == created.id()),
                "Neu gespeicherte Person muss im Farbfilter auftauchen"
        );
    }
}
