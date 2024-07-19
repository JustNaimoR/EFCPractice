package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MainTest {
    RedisMap redisMap = new RedisMap();
    // Использую для тестов
    Map<String, String> sample = Map.of(
            "key1", "value1",
            "key2", "value2",
            "key3", "value3"
    );


    @BeforeEach
    void addSample() {
        redisMap.putAll(sample);
    }

    @AfterEach
    void clearMap() {
        redisMap.clear();
    }



    // get() + put()
    @Test
    void putGetTest() {
        String key = "key";
        String value = "value";

        redisMap.put(key, value);

        assertEquals(redisMap.get(key), value);
    }

    // putAll()
    @Test
    void putAllTest() {
        int expected = sample.size();

        assertEquals(redisMap.size(), expected);
    }

    @Test
    void emptyTest() {
        redisMap.clear();
        assertTrue(redisMap.isEmpty());
    }

    @Test
    void containsKeyValueTest() {
        assertTrue(redisMap.containsKey("key1"));
        assertTrue(redisMap.containsValue("value1"));
    }

    @Test
    void removeTest() {
        String key = sample.keySet().stream().findAny().get();  //todo проверка на isPresent?
        String value = sample.get(key);

        String removedVal = redisMap.remove(key);

        assertFalse(redisMap.containsKey(key));
        assertFalse(redisMap.containsValue(value));
        assertEquals(value, removedVal);
    }

    @Test
    void keySetTest() {
        assertTrue(sample.keySet().containsAll(redisMap.keySet()));
    }

    @Test
    void valuesTest() {
        assertTrue(sample.values().containsAll(redisMap.values()));
    }

    @Test
    void entrySetTest() {
        assertEquals(redisMap.entrySet(), sample.entrySet());
    }
}