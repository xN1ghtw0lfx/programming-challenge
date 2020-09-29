package de.exxcellent.challenge.csv;

/**
 * Main Exception thrown by {@link CsvMapper} when encountering errors.
 *
 * @author Kevin Degen
 */
public class CsvParsingException extends RuntimeException {

    public CsvParsingException(String message) {
        super(message);
    }

    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
