package demo;

import demo.annotations.CSValue;
import demo.enums.Delimiter;
import demo.enums.WriteStrategy;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVriterTest {
    private final CSVriter vriter = CSVriter.getInstance();
    private final String filepath = "file.csv";



    @AfterEach
    void deleteFile() {
        new File(filepath).delete();
    }

    // Обычная запись объекта в файл и проверка на корректность итогового файла
    @Test
    void writeSimplePersonTest() {
        Person person = new Person("Alex", 24);

        vriter.writeToFile(person, filepath);

        String[] expected = { "name,age", "%s,%d".formatted(person.name, person.age) };

        checkFileContent(expected);
    }

    // Если внутри записываемой строки лежит разделитель - использование обрамления "_"
    @Test
    void valueWithDelimiterTest() {
        Person person = new Person("p1, p2, p3", 25);

        vriter.writeToFile(person, filepath);

        String[] expected = { "name,age", "\"%s\",%d".formatted(person.name, person.age) };

        checkFileContent(expected);
    }

    // запись списка людей в файл + проверка изменения разделителя и включение названия полей
    @Test
    void writePeopleListTest() {
        Person p1 = new Person("p1", 11);
        Person p2 = new Person("p2", 22);
        Person p3 = new Person("p3", 33);
        List<Person> people = List.of(p1, p2, p3);

        CSVriter.CSVSettings settings = CSVriter.CSVSettings.builder()
                .delimiter(Delimiter.SEMICOLON)
                .addHeaders(false)
                .build();

        String[] expected = {
                "%s;%d".formatted(p1.name, p1.age),
                "%s;%d".formatted(p2.name, p2.age),
                "%s;%d".formatted(p3.name, p3.age)
        };

        vriter.writeToFile(people, filepath, settings);

        checkFileContent(expected);
    }

    // Проверка корректного вывода ссылок
    @Test
    void fieldLinksTest() {
        Person friend1 = new Person("John", 12);
        Person friend2 = new Person("Alice", 23);
        Credentials creds = new Credentials("login", "password");
        ExtendedPerson person = new ExtendedPerson("Oleg", 19, List.of(friend1, friend2), creds);

        vriter.writeToFile(person, filepath);

        // дополнительные двойные кавычки нужны т.к. при перечислении элементов используются запятые
        String[] expected = { "name,friends,creds",
                "%s,%s,%s".formatted(person.name, "\"" + List.of(friend1, friend2) + "\"", creds.toString()) };

        checkFileContent(expected);
    }

    // Проверка на добавление в конец файла
    @Test
    void appendFileTest() {
        Person person1 = new Person("p1", 10);
        Person person2 = new Person("p2", 15);

        CSVriter.CSVSettings settings = CSVriter.CSVSettings.builder()
                .writeStrategy(WriteStrategy.APPEND)
                .build();

        vriter.writeToFile(person1, filepath);
        vriter.writeToFile(person2, filepath, settings);

        String[] expected = { "name,age",
                              "p1,10",
                              "p2,15"};

        checkFileContent(expected);
    }

    // Проверка записи в файл лишь помеченных полей
    @Test
    void notAllFieldsTest() {
        ExtendedPerson extPerson = new ExtendedPerson("Alex", 10, Collections.emptyList(), new Credentials("login", "password"));

        vriter.writeToFile(extPerson, filepath);

        // Отсутствует поле 'age'
        String[] expected = { "name,friends,creds",
                              "%s,%s,%s".formatted(extPerson.name, extPerson.friends, extPerson.creds) };

        checkFileContent(expected);
    }


    private void checkFileContent(String[] expected) {
        try (FileReader reader = new FileReader(filepath)) {
            BufferedReader buffReader = new BufferedReader(reader);

            for (String exp: expected) {
                assertEquals(exp, buffReader.readLine());
            }

            assertNull(buffReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }


    // Класс человека для тестирования
    @AllArgsConstructor
    private static class Person {
        @CSValue
        public String name;
        @CSValue
        public int age;

        @Override
        public String toString() {
            return "{%s %d}".formatted(name, age);
        }
    }

    // Расширенный человек для тестирования ссылок-полей
    @AllArgsConstructor
    private static class ExtendedPerson {
        @CSValue
        public String name;
        public int age;         // Специально не включен в добавление
        @CSValue
        public List<Person> friends;
        @CSValue
        private Credentials creds;

        @Override
        public String toString() {
            return "%s %s %s".formatted(name, friends, creds);
        }
    }

    // Логин-пароль человека
    @AllArgsConstructor
    private static class Credentials {
        private final String login;
        private String password;

        @Override
        public String toString() {
            return "{%s %s}".formatted(login, password);
        }
    }
}