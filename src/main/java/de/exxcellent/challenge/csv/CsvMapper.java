package de.exxcellent.challenge.csv;

import de.exxcellent.challenge.CsvProperty;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CsvMapper {

    private final CsvConfig config;

    public CsvMapper(CsvConfig config) {
        this.config = config;
    }

    public CsvMapper() {
        this.config = CsvConfig.defaultConfig();
    }

    public <T> List<T> fromCsv(String csv, Class<T> type) {
        return fromCsv(csv, StandardCharsets.UTF_8, type);
    }

    public <T> List<T> fromCsv(String csv, Charset charset, Class<T> type) {
        return fromCsv(new ByteArrayInputStream(csv.getBytes(charset)), type);
    }

    public <T> List<T> fromCsv(InputStream in, Class<T> type) {
        try {
            return doTheMagic(in, type);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> List<T> doTheMagic(InputStream in, Class<T> type) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {

        var bis = new BufferedReader(new InputStreamReader(in));

        String header = bis.readLine();
        String[] headers = splitLine(header);

        List<Field> fields = Arrays.stream(headers)
                .map(h -> Arrays.stream(type.getDeclaredFields())
                        .filter(f -> f.getAnnotation(CsvProperty.class) != null)
                        .filter(f -> h.equals(f.getAnnotation(CsvProperty.class).value()))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<T> objects = new ArrayList<>();
        String line;
        int lineCounter = 1;
        while ((line = bis.readLine()) != null) {
            String[] values = splitLine(line);
            if (!config.isIgnoreInvalidLines() && values.length != fields.size()) {
                throw new IllegalStateException(MessageFormat.format("Line {0} has {1} values but {2} has only {3} annotated fields",
                        lineCounter,
                        values.length,
                        type.getSimpleName(),
                        fields.size()));
            }

            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
            for (int i = 0; i < fields.size(); i++) {
                Field declaredField = type.getDeclaredField(fields.get(i).getName());
                declaredField.setAccessible(true);
                Class<?> fieldType = declaredField.getType();
                if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    declaredField.set(instance, Integer.parseInt(values[i]));
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    declaredField.set(instance, Double.parseDouble(values[i]));
                } else if (fieldType.equals(String.class)) {
                    declaredField.set(instance, values[i]);
                } else {
                    throw new RuntimeException("Type " + fieldType.getName() + " is not supported.");
                }
            }
            objects.add(instance);

            lineCounter++;
        }

        return objects;
    }

    private String[] splitLine(String header) {
        return header.split(Pattern.quote(Character.toString(config.getSeparator())));
    }

}
