package de.craften.plugins.playerdatastore.plugin;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A player data store that uses Redis.
 */
public class RedisPlayerDataStore implements PlayerDataStore {
    private final JedisPool pool;
    private final String uuid;

    public RedisPlayerDataStore(JedisPool pool, UUID uuid) {
        this.pool = pool;
        this.uuid = uuid.toString();
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hget(uuid, key);
        }
    }

    @Override
    public Map<String, String> getAll() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hgetAll(uuid);
        }
    }

    @Override
    public void put(String key, String value) {
        if (value != null) {
            try (Jedis jedis = pool.getResource()) {
                jedis.hset(uuid, key, value);
            }
        } else {
            remove(key);
        }
    }

    @Override
    public void putAll(Map<String, String> values) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hmset(uuid, values);
        }
    }

    @Override
    public void update(String key, Function<String, String> update) {
        // locking algorithm inspired by http://stackoverflow.com/a/22378859
        try (Jedis jedis = pool.getResource()) {
            String lockKey = uuid + ":" + key + ":lock";
            jedis.watch(lockKey);
            String oldValue = jedis.hget(uuid, key);
            String newValue = update.apply(oldValue);
            Transaction t = jedis.multi();
            t.set(lockKey, "");
            t.expire(lockKey, 3);
            t.hset(uuid, key, newValue);
            t.exec();
        }
    }

    @Override
    public String remove(String key) {
        try (Jedis jedis = pool.getResource()) {
            String old = get(key);
            jedis.hdel(uuid, key);
            return old;
        }
    }

    @Override
    public void clear() {
        try (Jedis jedis = pool.getResource()) {
            jedis.hkeys(uuid).forEach(this::remove);
        }
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
    public CompletableFuture<Void> updateAsync(String key, Function<String, String> update) {
        return CompletableFuture.runAsync(() -> update(key, update));
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
