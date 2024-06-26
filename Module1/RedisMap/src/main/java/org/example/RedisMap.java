package org.example;

import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

public class RedisMap implements Map<String, String> {
    private final Jedis jedis = new Jedis();

    @Override
    public String get(Object key) {
        return jedis.get((String) key);
    }

    @Override
    public String put(String key, String value) {
        return jedis.set(key, value);
    }

    @Override
    public int size() {
        return (int) jedis.dbSize();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains((String) value);
    }

    @Override
    public String remove(Object key) {
        return jedis.getDel((String) key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        for (Entry<? extends String, ? extends String> entry: m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (String key: keySet()) {
            jedis.del(key);
        }
    }

    @Override
    public Set<String> keySet() {
        return jedis.keys("*");
    }

    @Override
    public Collection<String> values() {
        return keySet().stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return keySet().stream()
                .map(key -> new AbstractMap.SimpleEntry<>(key, get(key)))
                .collect(Collectors.toSet());
    }
}