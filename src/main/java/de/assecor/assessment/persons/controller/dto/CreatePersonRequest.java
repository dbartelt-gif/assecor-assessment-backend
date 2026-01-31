package de.assecor.assessment.persons.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public record CreatePersonRequest(

        @NotBlank(message = "name darf nicht leer sein")
        String name,

        @NotBlank(message = "lastname darf nicht leer sein")
        String lastname,

        @NotBlank(message = "city darf nicht leer sein")
        String city,

        @NotBlank(message = "color darf nicht leer sein")
        @Pattern(
                regexp = "blau|grün|violett|rot|gelb|türkis|weiß",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "color muss eine gültige Farbe sein"
        )
        String color
) {}