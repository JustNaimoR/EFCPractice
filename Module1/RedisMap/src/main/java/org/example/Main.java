package org.example;

import java.util.function.Supplier;

public class Main {
    public static void main( String[] args ) {
        RedisMap map = new RedisMap();

        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        System.out.println(map.get("key1"));
        System.out.println(map.get("key2"));
        System.out.println(map.get("key3"));

        System.out.println(map.size());
        System.out.println(map.get("key20"));


        System.out.println(map.keySet());
    }
}
