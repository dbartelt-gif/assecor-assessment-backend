package de.assecor.assessment.persons.controller;


import java.util.List;

import de.assecor.assessment.persons.controller.dto.CreatePersonRequest;
import de.assecor.assessment.persons.model.Person;
import de.assecor.assessment.persons.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public List<Person> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/color/{color}")
    public List<Person> getByColor(@PathVariable String color) {
        return service.getByColor(color);
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody CreatePersonRequest req) {
        Person created = service.create(new Person(
                0,
                req.name(),
                req.lastname(),
                req.city(),
                req.color()
        ));
        return ResponseEntity.status(201).body(created);
    }
}