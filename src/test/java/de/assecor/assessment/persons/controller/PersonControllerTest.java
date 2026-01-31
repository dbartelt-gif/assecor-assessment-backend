package de.assecor.assessment.persons.controller;

import de.assecor.assessment.persons.model.Person;
import de.assecor.assessment.persons.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService service;

    @Test
    void getAll_returns200_andList() throws Exception {
        List<Person> persons = List.of(
                new Person(1, "Hans", "Müller", "67742 Lauterecken", "blau"),
                new Person(2, "Peter", "Petersen", "18439 Stralsund", "grün")
        );
        Mockito.when(service.getAll()).thenReturn(persons);

        mockMvc.perform(get("/persons").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hans"))
                .andExpect(jsonPath("$[0].lastname").value("Müller"))
                .andExpect(jsonPath("$[0].city").value("67742 Lauterecken"))
                .andExpect(jsonPath("$[0].color").value("blau"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].color").value("grün"));

        Mockito.verify(service).getAll();
    }

    @Test
    void getById_returns200_whenFound() throws Exception {
        Person p = new Person(1, "Hans", "Müller", "67742 Lauterecken", "blau");
        Mockito.when(service.getById(1)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/persons/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hans"))
                .andExpect(jsonPath("$.lastname").value("Müller"))
                .andExpect(jsonPath("$.city").value("67742 Lauterecken"))
                .andExpect(jsonPath("$.color").value("blau"));

        Mockito.verify(service).getById(1);
    }

    @Test
    void getById_returns404_whenNotFound() throws Exception {
        Mockito.when(service.getById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/persons/999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(service).getById(999);
    }

    @Test
    void getByColor_returns200_andFilteredList() throws Exception {
        List<Person> blues = List.of(
                new Person(1, "Hans", "Müller", "67742 Lauterecken", "blau"),
                new Person(3, "Johnny", "Johnson", "88888 made up", "blau")
        );
        Mockito.when(service.getByColor("blau")).thenReturn(blues);

        mockMvc.perform(get("/persons/color/blau").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].color").value("blau"))
                .andExpect(jsonPath("$[1].color").value("blau"));

        Mockito.verify(service).getByColor("blau");
    }
}
