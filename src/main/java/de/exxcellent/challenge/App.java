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

        var mapper = new CsvMapper();

        var weatherData = mapper.fromCsv(App.class.getResourceAsStream("weather.csv"), WeatherData.class);
        System.out.printf("Day with smallest temperature spread : %d%n", getMin(weatherData, WeatherData::getTempSpread).getDay());

        var footballData = mapper.fromCsv(App.class.getResourceAsStream("football.csv"), FootballData.class);
        System.out.printf("Team with smallest goal spread       : %s%n", getMin(footballData, FootballData::getGoalSpread).getTeam());
    }

    private static <T> T getMin(List<T> elements, Function<T, Integer> function) {

        T minObj = null;
        var minValue = -1;

        for (var element : elements) {
            var number = function.apply(element);
            if (minValue < 0 || number < minValue) {
                minObj = element;
                minValue = number;
            }
        }

        return minObj;
    }
}
