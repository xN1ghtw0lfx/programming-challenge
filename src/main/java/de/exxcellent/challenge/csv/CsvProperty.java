package de.exxcellent.challenge.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields for the {@link CsvMapper} to map.
 *
 * @author Kevin Degen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvProperty {

    /**
     * The column name associated with the annotated field.
     */
    String value();

}
