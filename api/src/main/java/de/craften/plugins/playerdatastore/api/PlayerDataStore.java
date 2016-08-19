package de.craften.plugins.playerdatastore.api;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A key-value store for player data.
 */
public interface PlayerDataStore {
    /**
     * Gets the value for the given key.
     *
     * @param key key
     * @return value for the given key, null if no value is set
     */
    String get(String key);

    /**
     * Gets all keys and values.
     *
     * @return all keys and values
     */
    Map<String, String> getAll();

    /**
     * Sets the given key to the given value. If the key already exists, it is overwritten. If the value is
     * <code>null</code>, the key is removed.
     *
     * @param key   key
     * @param value value
     */
    void put(String key, String value);

    /**
     * Sets all given keys to the given values, according to the given map. No value may be <code>null</code>. Existing
     * values are replaced.
     *
     * @param values key-value map
     */
    void putAll(Map<String, String> values);

    /**
     * Atomically updates the value of the given key.
     *
     * @param key    key
     * @param update update function that gets the old value and returns the new value
     */
    void update(String key, Function<String, String> update);

    /**
     * Removes the given key and its value.
     *
     * @param key key
     * @return the previous value of this key
     */
    String remove(String key);

    /**
     * Removes all keys. This doesn't guarantee that this store is empty after calling this method. There may be new
     * values inserted while this runs.
     */
    void clear();

    /**
     * Gets the value for the given key.
     *
     * @param key key
     * @return future with the value for the given key, null if no value is set
     */
    CompletableFuture<String> getAsync(String key);

    /**
     * Gets all keys and values.
     *
     * @return future with all keys and values
     */
    CompletableFuture<Map<String, String>> getAllAsync();

    /**
     * Sets the given key to the given value. If the key already exists, it is overwritten. If the value is
     * <code>null</code>, the key is removed.
     *
     * @param key   key
     * @param value value
     * @return future that can be used to wait for this action
     */
    CompletableFuture<Void> putAsync(String key, String value);

    /**
     * Sets all given keys to the given values, according to the given map. No value may be <code>null</code>. Existing
     * values are replaced.
     *
     * @param values key-value map
     * @return future that can be used to wait for this action
     */
    CompletableFuture<Void> putAllAsync(Map<String, String> values);

    /**
     * Removes the given key and its value.
     *
     * @param key key
     * @return future with the previous value of this key
     */
    CompletableFuture<String> removeAsync(String key);

    /**
     * Removes all keys. This doesn't guarantee that this store is empty after calling this method. There may be new
     * values inserted while this runs.
     *
     * @return future that can be used to wait for this action
     */
    CompletableFuture<Void> clearAsync();

    /**
     * Atomically updates the value of the given key.
     *
     * @param key    key
     * @param update update function that gets the old value and returns the new value
     * @return future that can be used to wait for this action
     */
    CompletableFuture<Void> updateAsync(String key, Function<String, String> update);
}
