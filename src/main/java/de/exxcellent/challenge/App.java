package de.exxcellent.challenge;

import de.exxcellent.challenge.csv.CsvConfig;
import de.exxcellent.challenge.csv.CsvMapper;
import de.exxcellent.challenge.data.FootballData;
import de.exxcellent.challenge.data.WeatherData;

import java.util.List;
import java.util.function.Function;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
public final class App {

    /**
     * This is the main entry method of your program.
     *
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

        CsvMapper mapper = new CsvMapper(CsvConfig.builder().ignoreInvalidLines().separator(';').build());

        List<WeatherData> weatherData = mapper.fromCsv(App.class.getResourceAsStream("weather.csv"), WeatherData.class);
        System.out.printf("Day with smallest temperature spread : %d%n", getMin(weatherData, WeatherData::getTempSpread).getDay());

        List<FootballData> footballData = mapper.fromCsv(App.class.getResourceAsStream("football.csv"), FootballData.class);
        System.out.printf("Team with smallest goal spread       : %s%n", getMin(footballData, FootballData::getGoalSpread).getTeam());
    }

    private static <T> T getMin(List<T> elements, Function<T, Integer> function) {

        T minObj = null;
        int minValue = -1;

        for (T element : elements) {
            Integer call = function.apply(element);
            if (minValue < 0 || call < minValue) {
                minObj = element;
                minValue = call;
            }
        }

        return minObj;
    }
}
