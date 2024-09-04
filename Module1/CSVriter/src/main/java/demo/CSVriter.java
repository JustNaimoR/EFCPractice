package demo;

import demo.annotations.CSValue;
import demo.enums.Delimiter;
import demo.enums.WriteStrategy;
import demo.exceptions.WriteIOException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/*
 * Singleton класс для работы с записью в файл с расширением .csv
 */
@Slf4j
public class CSVriter {
    private static CSVriter INSTANCE;

    /*
     * Класс настроек записи в CSV-файл.
     *
     * По умолчанию стоят настройки:
     * - Разделитель ','
     * - Создание нового файла для каждой записи
     * - Добавление названий колонок в начало .csv файла
     */
    @Builder
    public static class CSVSettings {
        public static final Delimiter DELIMITER_DEFAULT = Delimiter.COMMA;                  // Используемый дефолтный разделитель данных
        public static final WriteStrategy WRITE_STRATEGY_DEFAULT = WriteStrategy.WRITE;     // Используемый способ для записи в файл (создать новый или добавить в старый)
        public static final boolean ADD_HEADERS_DEFAULT = true;                             // Флаг добавления заголовков в файл (название каждой колонки)

        @Builder.Default
        public Delimiter delimiter = DELIMITER_DEFAULT;     //todo есть ли смысл делать поля private для класса-конфигурации...
        @Builder.Default
        public WriteStrategy writeStrategy = WRITE_STRATEGY_DEFAULT;
        @Builder.Default
        public boolean addHeaders = ADD_HEADERS_DEFAULT;

        public static CSVSettings getDefault() {
            return CSVSettings.builder().build();
        }

        public void resetSettings() {
            delimiter = DELIMITER_DEFAULT;
            writeStrategy = WRITE_STRATEGY_DEFAULT;
            addHeaders = ADD_HEADERS_DEFAULT;
        }

        @Override
        public String toString() {
            return "[delimiter='%s', writeStrategy='%s', addHeaders='%s']".formatted(
                    delimiter, writeStrategy, addHeaders
            );
        }
    }

    private CSVriter() {}

    public static CSVriter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CSVriter();
        }
        return INSTANCE;
    }


    public <T> void writeToFile(T t, String filepath) {
        writeToFile(Collections.singletonList(t), filepath, CSVSettings.getDefault());
    }

    public <T> void writeToFile(T t, String filepath, CSVSettings settings) {
        writeToFile(Collections.singletonList(t), filepath, settings);
    }

    public <T> void writeToFile(List<T> list, String filepath) {
        writeToFile(list, filepath, CSVSettings.getDefault());
    }

    // Запись объектов из 'list' в файл по пути 'filepath' с настройками 'settings'
    public <T> void writeToFile(List<T> list, String filepath, CSVSettings settings) {
        if (list.isEmpty())
            return;

        try (FileWriter writer = new FileWriter(filepath, settings.writeStrategy.get())) {
            if (settings.addHeaders && settings.writeStrategy != WriteStrategy.APPEND) {
                String columns = fieldsToString(list.get(0).getClass(), settings.delimiter) + '\n';
                writer.write(columns);
            }

            for (T t: list) {
                String curString = valuesToString(t, settings.delimiter) + '\n';
                writer.write(curString);
            }

        } catch (IOException e) {
            log.error("Got exception while writing to a file");
            throw new WriteIOException();
        }

        //todo Излишне ли такое логирование? Или в логи выносить лишь warnings и errors
        log.info("%d objects were written to a file along the path %s with settings - %s".formatted(
                list.size(), filepath, settings
        ));
    }



    // Представление значений полей объекта T в виде строки
    private <T> String valuesToString(T t, Delimiter delimiter) {
        Function<Field, String> mapping = (Field f) -> {
            try {
                f.setAccessible(true);
                String val = String.valueOf(f.get(t));
                if (val.contains(delimiter.toString()))
                    return "\"%s\"".formatted(val);
                return val;
            } catch (IllegalAccessException e) {
                log.error("Got exception while trying to parse a field - %s".formatted(f.getName()));
                throw new WriteIOException();
            } finally {
                f.setAccessible(false);
            }
        };

        Field[] fs = t.getClass().getDeclaredFields();

        return String.join(delimiter.toString(), fieldsToStringsMapping(fs, mapping));
    }

    // Представление полей класса в виде строки
    private String fieldsToString(Class<?> clazz, Delimiter delimiter) {
        Function<Field, String> mapping = (Field f) -> {
            String name = f.getName();
            if (name.contains(delimiter.toString()))
                return "\"%s\"".formatted(name);
            return name;
        };

        Field[] fs = clazz.getDeclaredFields();

        return String.join(delimiter.toString(), fieldsToStringsMapping(fs, mapping));
    }

    // Фильтрация полей и получения списка замапенных полей
    private List<String> fieldsToStringsMapping(Field[] fs, Function<Field, String> mapping) {
        return Arrays.stream(fs)
                .filter(f -> f.isAnnotationPresent(CSValue.class))
                .map(mapping)
                .toList();
    }
}