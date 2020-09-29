package de.exxcellent.challenge.csv;

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

    public static class Builder {

        private char separator = ',';

        private boolean ignoreInvalidLines = false;

        private Builder() {
        }

        public Builder separator(char separator) {
            this.separator = separator;
            return this;
        }

        public Builder ignoreInvalidLines() {
            this.ignoreInvalidLines = true;
            return this;
        }

        public CsvConfig build() {
            return new CsvConfig(this);
        }

    }
}
