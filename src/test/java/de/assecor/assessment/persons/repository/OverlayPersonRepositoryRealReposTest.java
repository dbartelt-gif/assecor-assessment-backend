package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OverlayPersonRepositoryRealReposTest {

    private CsvPersonRepository csvRepo;
    private InMemoryPersonRepository memRepo;
    private OverlayPersonRepository overlay;

    @BeforeEach
    void setUp() {
        csvRepo = new CsvPersonRepository();
        memRepo = new InMemoryPersonRepository();
        overlay = new OverlayPersonRepository(csvRepo, memRepo);
        overlay.initIdSequence();
    }

    @Test
    void findAll_containsAtLeastAllCsvPersons() {
        int csvCount = csvRepo.findAll().size();
        int overlayCount = overlay.findAll().size();

        assertTrue(csvCount > 0, "CSV sollte mindestens einen Datensatz enthalten");
        assertEquals(csvCount, overlayCount, "Ohne Memory-Einträge muss Overlay = CSV sein");
    }

    @Test
    void save_addsPersonToOverlay_andIncreasesCount() {
        int before = overlay.findAll().size();

        Person created = overlay.save(new Person(0, "Test", "Person", "TestCity", "rot"));

        int after = overlay.findAll().size();

        assertTrue(created.id() > 0, "ID sollte beim Speichern vergeben werden");
        assertEquals(before + 1, after, "Nach save() muss die Anzahl um 1 steigen");

        Person fetched = overlay.findById(created.id()).orElseThrow();
        assertEquals("Test", fetched.name());
        assertEquals("rot", fetched.color());
    }

    @Test
    void findById_prefersMemory_whenSameIdExistsInCsv() {
        Person anyCsv = csvRepo.findAll().getFirst();

        Person override = new Person(anyCsv.id(), anyCsv.name(), anyCsv.lastname(), "UPDATED CITY", "rot");
        memRepo.save(override);

        Person result = overlay.findById(anyCsv.id()).orElseThrow();
        assertEquals("UPDATED CITY", result.city());
        assertEquals("rot", result.color());
    }

    @Test
    void findByColor_includesCsvAndMemoryResults() {
        String existingColor = csvRepo.findAll().getFirst().color();

        int csvMatches = csvRepo.findByColor(existingColor).size();

        Person created = overlay.save(new Person(0, "Color", "Match", "Somewhere", existingColor));

        List<Person> overlayMatches = overlay.findByColor(existingColor);

        assertEquals(csvMatches + 1, overlayMatches.size(), "Overlay sollte CSV-Matches + Memory-Match enthalten");
        assertTrue(overlayMatches.stream().anyMatch(p -> p.id() == created.id()),
                "Neu gespeicherte Person muss im Farbfilter enthalten sein");
    }

    @Test
    void initIdSequence_setsMemoryIdAfterMaxCsvId() {
        int maxCsvId = csvRepo.findAll().stream()
                .map(Person::id)
                .max(Comparator.naturalOrder())
                .orElse(0);

        Person created = overlay.save(new Person(0, "Seq", "Test", "City", "blau"));

        assertTrue(created.id() > maxCsvId,
                "Neue ID muss größer als die höchste CSV-ID sein (damit keine Kollision entsteht)");
    }
}
