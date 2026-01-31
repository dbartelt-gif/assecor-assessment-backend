package de.assecor.assessment.persons.repository;

import de.assecor.assessment.persons.model.Person;
import de.assecor.assessment.persons.util.CSVCleanUp;
import de.assecor.assessment.persons.util.ColorMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Qualifier("csvRepo")
public class CsvPersonRepository implements PersonRepository {

    public static void main(String[] args) {
       new CsvPersonRepository();
    }

    private final List<Person> persons = new ArrayList<>();

    public CsvPersonRepository() {
        loadCsv();
    }

    private void loadCsv() {
        if(!persons.isEmpty()) persons.clear();
        String cleanedCsv = new CSVCleanUp().cleanUp(new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("sample-input.csv")))));
        try (BufferedReader reader = new BufferedReader(new java.io.StringReader(cleanedCsv))) {
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                createPersonFromLine(line, i);
                i++;
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV konnte nicht geladen werden", e);
        }
    }

    private void createPersonFromLine(String line, int i) {
        String[] split = line.split(",");

        String color = ColorMapper.fromId(Integer.parseInt(split[3].trim()));

        persons.add(new Person(i,split[1].trim(),split[0],split[2].trim(),color));
    }

    @Override
    public List<Person> findAll() {
        return persons;
    }

    @Override
    public Optional<Person> findById(int id) {
        return persons.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }

    @Override
    public List<Person> findByColor(String color) {
        return persons.stream()
                .filter(p -> p.color().equalsIgnoreCase(color))
                .toList();
    }

    @Override
    public Person save(Person person) {
        throw new UnsupportedOperationException("CSV ist read-only (sample-input.csv wird nicht verändert).");
    }
}
