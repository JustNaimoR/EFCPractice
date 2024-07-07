package edu.mod4;

import demo.CSVriter;
import demo.annotations.CSValue;

public class App {
    public static void main( String[] args ) {
        class Person {
            @CSValue
            private String name = "John";
            @CSValue
            private int age = 24;
        }

        CSVriter vriter = CSVriter.getInstance();
        vriter.writeToFile(new Person(), "person.csv");
    }
}