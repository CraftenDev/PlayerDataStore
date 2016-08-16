package de.craften.plugins.playerdatastore.plugin;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A player data store that uses Redis.
 */
public class RedisPlayerDataStore implements PlayerDataStore {
    private final Jedis jedis;
    private final String uuid;

    public RedisPlayerDataStore(Jedis jedis, UUID uuid) {
        this.jedis = jedis;
        this.uuid = uuid.toString();
    }

    @Override
    public String get(String key) {
        return jedis.hget(uuid, key);
    }

    @Override
    public Map<String, String> getAll() {
        return jedis.hgetAll(uuid);
    }

    @Override
    public void put(String key, String value) {
        if (value != null) {
            jedis.hset(uuid, key, value);
        } else {
            remove(key);
        }
    }

    @Override
    public void putAll(Map<String, String> values) {
        jedis.hmset(uuid, values);
    }

    @Override
    public String remove(String key) {
        String old = get(key);
        jedis.hdel(uuid, key);
        return old;
    }

    @Override
    public void clear() {
        jedis.hkeys(uuid).forEach(this::remove);
    }

    @Override
    public CompletableFuture<String> getAsync(String key) {
        return CompletableFuture.supplyAsync(() -> get(key));
    }

    @Override
    public CompletableFuture<Map<String, String>> getAllAsync() {
        return CompletableFuture.supplyAsync(this::getAll);
    }

    @Override
    public CompletableFuture<Void> putAsync(String key, String value) {
        return CompletableFuture.runAsync(() -> put(key, value));
    }

    @Override
    public CompletableFuture<Void> putAllAsync(Map<String, String> values) {
        return CompletableFuture.runAsync(() -> putAll(values));
    }

    @Override
    public CompletableFuture<String> removeAsync(String key) {
        return CompletableFuture.supplyAsync(() -> remove(key));
    }

    @Override
    public CompletableFuture<Void> clearAsync() {
        return CompletableFuture.runAsync(this::clear);
    }
}
