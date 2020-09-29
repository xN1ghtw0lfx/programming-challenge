package de.exxcellent.challenge.csv;

/**
 * Configuration object for {@link CsvMapper}. Currently you can only change the separator and how to handle lines which are invalid.
 * A builder instance can be created by calling {@link #builder()}.
 *
 * @author Kevin Degen
 */
public class CsvConfig {

    private final char separator;

    private final boolean ignoreInvalidLines;

    public CsvConfig(Builder builder) {
        this.separator = builder.separator;
        this.ignoreInvalidLines = builder.ignoreInvalidLines;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static CsvConfig defaultConfig() {
        return new Builder().build();
    }

    public char getSeparator() {
        return separator;
    }

    public boolean isIgnoreInvalidLines() {
        return ignoreInvalidLines;
    }

    /**
     * Builder for {@link CsvConfig}.
     */
    public static class Builder {

        private char separator = ',';

        private boolean ignoreInvalidLines = false;

        private Builder() {
        }

        /**
         * Sets the separator to be used by the {@link CsvMapper}.
         */
        public Builder separator(char separator) {
            this.separator = separator;
            return this;
        }

        /**
         * Tells {@link CsvMapper} to skip lines which are invalid. Invalid lines have less or more values than the object has annotated fields.
         * The default behaviour is to throw an {@link CsvParsingException} when encountering an invalid line.
         */
        public Builder ignoreInvalidLines() {
            this.ignoreInvalidLines = true;
            return this;
        }

        /**
         * Builds the {@link CsvConfig} object.
         */
        public CsvConfig build() {
            return new CsvConfig(this);
        }

    }
}
