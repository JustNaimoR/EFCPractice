package org.example;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RedisMap implements Map<String, String> {
    private final Jedis jedis = new Jedis();

    @Override
    public String get(Object key) {
        try {
            return jedis.get((String) key);
        } catch (JedisException ex) {
            log.error("Jedis exception at jedis.get()", ex);
            return "";        //todo null или ""? null вроде опаснее
        }
    }

    @Override
    public String put(String key, String value) {
        try {
            return jedis.set(key, value);
        } catch (JedisException ex) {
            log.error("Jedis exception at jedis.put()", ex);
            return "";
        }
    }

    @Override
    public int size() {
        try {
            return (int) jedis.dbSize();
        } catch (JedisException ex) {
            log.error("Jedis exception at jedis.size()", ex);
            return -1;
        }
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
        try {
            return jedis.getDel((String) key);
        } catch (JedisException ex) {
            log.error("Jedis exception at jedis.remove()", ex);
            return "";
        }
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
            remove(key);
        }
    }

    @Override
    public Set<String> keySet() {
        try {
            return new HashSet<>(jedis.scan("0").getResult());
        } catch (JedisException ex) {
            log.error("Jedis exception at jedis.put()", ex);
            return Collections.emptySet();
        }
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