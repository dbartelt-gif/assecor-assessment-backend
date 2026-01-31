package de.assecor.assessment.persons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVCleanUp {

    /**
     * "Räumt" inkonsistente CSV-ähnliche Daten im Speicher auf:
     * - fügt Zeilen zusammen, bis ein vollständiger Datensatz entsteht
     * - ein Datensatz ist vollständig, wenn das letzte (durch Komma getrennte) Feld eine Zahl ist (colorId)
     * - Ergebnis: ein String mit genau einer Zeile pro Datensatz (durch \n getrennt)
     */
    public String cleanUp(BufferedReader reader) {
        List<String> cleanedLines = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isBlank()) {
                    if (!buffer.isEmpty()) buffer.append(' ');
                    buffer.append(line);
                    if (isCompleteRecord(buffer.toString())) {
                        cleanedLines.add(buffer.toString());
                        buffer.setLength(0);
                    }
                }
            }

            return String.join("\n", cleanedLines);

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Lesen der CSV", e);
        }
    }

    private boolean isCompleteRecord(String s) {
        String[] parts = s.split(",", -1);
        if (parts.length < 4) return false;

        String last = parts[parts.length - 1].trim();
        return last.matches("\\d+");
    }
}
