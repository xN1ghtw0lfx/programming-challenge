package de.exxcellent.challenge.csv;

import de.exxcellent.challenge.data.FootballData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvMapperTest {

    @Test
    void fromCsvString() throws URISyntaxException, IOException {
        var csvStr = Files.readString(Path.of(CsvMapperTest.class.getResource("football.csv").toURI()));
        var mapper = new CsvMapper();
        var footballData = mapper.fromCsv(csvStr, FootballData.class);

        assertEquals(20, footballData.size());
    }

    @Test
    void fromCsvInputStream() {
        var mapper = new CsvMapper();
        var footballData = mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("football.csv"), FootballData.class);

        assertEquals(20, footballData.size());
    }

    @Test
    void customSeparator() {
        var config = CsvConfig.builder()
                .separator(';')
                .build();
        var mapper = new CsvMapper(config);

        var footballData = mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("footballSemi.csv"), FootballData.class);

        assertEquals(20, footballData.size());
    }

    @Test
    void invalidLines() {
        var mapper = new CsvMapper();

        assertThrows(CsvParsingException.class, () -> mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("footballIncomplete.csv"), FootballData.class));
    }

    @Test
    void ignoreInvalidLines() {
        var config = CsvConfig.builder()
                .ignoreInvalidLines()
                .build();
        var mapper = new CsvMapper(config);

        var footballData = mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("footballIncomplete.csv"), FootballData.class);

        assertEquals(17, footballData.size());
    }
}