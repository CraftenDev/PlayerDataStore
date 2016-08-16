package de.craften.plugins.playerdatastore.api;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A key-value store for player data.
 */
public interface PlayerDataStore {
    String get(String key);

    Map<String, String> getAll();

    void put(String key, String value);

    void putAll(Map<String, String> values);

    String remove(String key);

    void clear();

    CompletableFuture<String> getAsync(String key);

    CompletableFuture<Map<String, String>> getAllAsync();

    CompletableFuture<Void> putAsync(String key, String value);

    CompletableFuture<Void> putAllAsync(Map<String, String> values);

    CompletableFuture<String> removeAsync(String key);

    CompletableFuture<Void> clearAsync();
}
