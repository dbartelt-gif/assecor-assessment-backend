package de.assecor.assessment.persons.util;


import java.util.Map;

public class ColorMapper {

    private ColorMapper(){}

    private static final Map<Integer, String> COLORS = Map.of(
            1, "blau",
            2, "grün",
            3, "violett",
            4, "rot",
            5, "gelb",
            6, "türkis",
            7, "weiß"
    );

    public static String fromId(int id) {
        return COLORS.getOrDefault(id, "unbekannt");
    }
}