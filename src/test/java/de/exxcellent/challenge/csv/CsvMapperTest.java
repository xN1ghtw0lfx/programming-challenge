package de.exxcellent.challenge.csv;

import de.exxcellent.challenge.csv.data.AbstractFootballData;
import de.exxcellent.challenge.csv.data.FootballDataException;
import de.exxcellent.challenge.csv.data.FootballDataNoDefaultConstructor;
import de.exxcellent.challenge.csv.data.WeatherDataInvalidDatatype;
import de.exxcellent.challenge.data.FootballData;
import de.exxcellent.challenge.data.WeatherData;
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
        var csvStr = Files.readString(Path.of(CsvMapperTest.class.getResource("weather.csv").toURI()));
        var mapper = new CsvMapper();
        var weatherData = mapper.fromCsv(csvStr, WeatherData.class);

        assertEquals(30, weatherData.size());
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

    @Test
    void noDefaultConstructor() {
        var mapper = new CsvMapper();

        assertThrows(CsvParsingException.class, () -> mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("football.csv"), FootballDataNoDefaultConstructor.class));
    }

    @Test
    void abstractDataClass() {
        var mapper = new CsvMapper();

        assertThrows(CsvParsingException.class, () -> mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("football.csv"), AbstractFootballData.class));
    }

    @Test
    void constructorException() {
        var mapper = new CsvMapper();

        assertThrows(CsvParsingException.class, () -> mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("football.csv"), FootballDataException.class));
    }

    @Test
    void invalidDataType() {
        var mapper = new CsvMapper();

        assertThrows(CsvParsingException.class, () -> mapper.fromCsv(CsvMapperTest.class.getResourceAsStream("weather.csv"), WeatherDataInvalidDatatype.class));
    }
}