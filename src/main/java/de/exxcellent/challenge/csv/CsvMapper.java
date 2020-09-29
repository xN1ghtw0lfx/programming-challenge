package de.exxcellent.challenge.csv;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

/**
 * The {@link CsvMapper} is able to turn a well formatted CSV file into a java object.
 * Every column header has to have a corresponding field annotated with {@link CsvProperty} in the java class.
 * Inspired by Jackson and Gson.
 *
 * <p> Potential enhancements:
 * <li> Support more than just UTF8.
 * <li> Add more config options, e.g. error handling and how to handle empty values.
 * <li> Make the mapper less strict. Currently every column in the csv needs an annotated field in the data class.
 * <li> Support more than int double and String. E.g. long, float, Date or even Enums.
 * <li> Use multiple Exceptions instead of a single Exception for every error.
 * <li> Implement a way back from the object to the CSV file.
 * <li> Limit the amount of available separators. Would make testing easier.
 * <li> Performance improvements.
 *
 * @author Kevin Degen
 */
public class CsvMapper {

    private final CsvConfig config;

    public CsvMapper(CsvConfig config) {
        this.config = config;
    }

    public CsvMapper() {
        this.config = CsvConfig.defaultConfig();
    }

    public <T> List<T> fromCsv(String csv, Class<T> type) {
        return fromCsv(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)), type);
    }

    public <T> List<T> fromCsv(InputStream in, Class<T> type) {
        return mapCsv(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)), type);
    }

    private <T> List<T> mapCsv(BufferedReader reader, Class<T> type) {
        var lineCounter = 1;

        var fields = mapFieldsToHeader(reader, type, lineCounter);
        lineCounter++;

        List<T> objects = new ArrayList<>();
        String line;

        while ((line = getLine(reader, lineCounter)) != null) {
            var values = splitLine(line);

            if (values.length != fields.size()) {
                if (config.isIgnoreInvalidLines()) {
                    continue;
                }

                throw new CsvParsingException(format("Line {0} has {1} values but {2} has only {3} annotated fields",
                        lineCounter,
                        values.length,
                        type.getSimpleName(),
                        fields.size()));
            }

            var constructor = getDeclaredConstructor(type);
            constructor.setAccessible(true);
            var instance = createInstance(constructor, type);

            for (var i = 0; i < fields.size(); i++) {
                var declaredField = getDeclaredField(type, fields, i);
                var value = values[i];
                setFieldValue(instance, declaredField, value);
            }

            objects.add(instance);
            lineCounter++;
        }


        return objects;


    }

    private <T> List<Field> mapFieldsToHeader(BufferedReader reader, Class<T> type, int lineCounter) {
        var header = getLine(reader, lineCounter);

        var headers = splitLine(header);

        return Arrays.stream(headers)
                .map(h -> Arrays.stream(type.getDeclaredFields())
                        .filter(f -> f.getAnnotation(CsvProperty.class) != null)
                        .filter(f -> h.equals(f.getAnnotation(CsvProperty.class).value()))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private <T> void setFieldValue(T instance, Field declaredField, String value) {
        declaredField.setAccessible(true);
        var fieldType = declaredField.getType();

        if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            setFieldValue(declaredField, instance, Integer.parseInt(value));
        } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
            setFieldValue(declaredField, instance, Double.parseDouble(value));
        } else if (fieldType.equals(String.class)) {
            setFieldValue(declaredField, instance, value);
        } else {
            throw new CsvParsingException(format("The type {0} is not supported.", fieldType.getName()));
        }
    }

    private String[] splitLine(String header) {
        return header.split(Pattern.quote(Character.toString(config.getSeparator())));
    }

    private <T> void setFieldValue(Field field, T instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            //Should never happen because we make them accessible.
            throw new CsvParsingException(format("Couldn't access the given method of {0}", instance.getClass().getName()), e);
        }
    }

    private <T> T createInstance(Constructor<T> constructor, Class<T> type) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException e) {
            throw new CsvParsingException(format("Can't instantiate an abstract class. Make sure that {0} isn't abstract.", type.getName()), e);
        } catch (InvocationTargetException e) {
            throw new CsvParsingException(format("There was an exception inside the constructor of {0}.", type.getName()), e);
        } catch (IllegalAccessException e) {
            //Should never happen because we make them accessible.
            throw new CsvParsingException("Couldn't access the given method of " + type.getName(), e);
        }
    }

    private String getLine(BufferedReader reader, int lineCounter) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new CsvParsingException("Error reading line " + lineCounter, e);
        }
    }

    private <T> Constructor<T> getDeclaredConstructor(Class<T> type) {
        try {
            return type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new CsvParsingException("Couldn't find the default constructor of " + type.getName() + ". The constructor can be private.", e);
        }
    }

    private Field getDeclaredField(Class<?> type, List<Field> fields, int i) {
        try {
            return type.getDeclaredField(fields.get(i).getName());
        } catch (NoSuchFieldException e) {
            //Should never happen because we get the field names directly from the class itself.
            throw new CsvParsingException("Couldn't access the given field of " + type.getName(), e);
        }
    }

}
