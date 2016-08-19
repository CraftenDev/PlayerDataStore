package de.craften.plugins.playerdatastore.plugin;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class RedisPlayerDataStoreTest {
    private RedisPlayerDataStore store;
    private RedisPlayerDataStore secondStore;

    @Before
    public void setup() throws URISyntaxException {
        store = new RedisPlayerDataStore(new Jedis(new URI("redis://localhost:6379/1")), UUID.randomUUID());
        secondStore = new RedisPlayerDataStore(new Jedis(new URI("redis://localhost:6379/1")), UUID.randomUUID());
    }

    @Test
    public void get() throws Exception {
        store.put("foo", "bar");
        assertEquals("bar", store.get("foo"));
        assertNull(secondStore.get("foo"));
    }

    @Test
    public void getAll() throws Exception {
        assertTrue(store.getAll().isEmpty());
        assertTrue(secondStore.getAll().isEmpty());

        store.put("foo", "bar");
        store.put("foo2", "bar2");
        assertEquals(2, store.getAll().size());
        assertEquals("bar", store.getAll().get("foo"));
        assertEquals("bar2", store.getAll().get("foo2"));
        assertTrue(secondStore.getAll().isEmpty());
    }

    @Test
    public void put() throws Exception {
        store.put("foo", "bar");
        assertEquals("bar", store.get("foo"));
        store.put("foo", "barbar");
        assertEquals("barbar", store.get("foo"));
        store.put("foo", null);
        assertEquals(null, store.get("foo"));

        assertEquals(null, secondStore.get("foo"));
    }

    @Test
    public void putAll() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("foo2", "bar2");
        store.putAll(map);

        assertEquals("bar", store.get("foo"));
        assertEquals("bar2", store.get("foo2"));
        assertEquals(map, store.getAll());

        store.put("someOtherKey", "value");
        store.putAll(map);
        assertEquals("value", store.get("someOtherKey"));

        assertEquals(0, secondStore.getAll().size());
    }

    @Test
    public void update() throws Exception {
        store.put("foo", "bar");
        store.update("foo", (old) -> {
            assertEquals("bar", old);
            return old + "2";
        });
        assertEquals("bar2", store.get("foo"));
    }

    @Test
    public void remove() throws Exception {
        store.put("foo", "bar");
        secondStore.put("foo", "bar");
        store.remove("foo");
        assertEquals(null, store.get("foo"));
        assertEquals("bar", secondStore.get("foo"));
    }

    @Test
    public void clear() throws Exception {
        store.put("foo", "bar");
        secondStore.put("foo", "bar");
        store.clear();
        assertEquals(0, store.getAll().size());
        assertEquals(1, secondStore.getAll().size());
    }
}