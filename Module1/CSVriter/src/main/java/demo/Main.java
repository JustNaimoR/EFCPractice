package demo;

import demo.annotations.CSValue;
import demo.enums.WriteStrategy;
import demo.exceptions.WriteIOException;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        CSVriter vriter = CSVriter.getInstance();
        String filepath = "file.txt";
        List<Person> people = List.of(
                new Person("Alex", 21),
                new Person("Oleg", 25),
                new Person("Vlad", 26)
        );

        vriter.writeToFile(people, filepath);
    }

    @AllArgsConstructor
    static class Person {
        @CSValue
        String name;
        @CSValue
        int age;
    }

}