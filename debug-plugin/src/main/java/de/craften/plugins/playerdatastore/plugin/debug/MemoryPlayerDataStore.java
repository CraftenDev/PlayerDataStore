package de.craften.plugins.playerdatastore.plugin.debug;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A player data store that uses a {@link ConfigurationSection} and that is NOT persistent.
 * For debugging use only.
 */
public class MemoryPlayerDataStore implements PlayerDataStore {
    private final ConfigurationSection section;
    private final String uuid;

    public MemoryPlayerDataStore(ConfigurationSection section, UUID uuid) {
        this.section = section;
        this.uuid = uuid.toString();
        if (!section.contains(this.uuid)) {
            section.set(this.uuid, new MemoryConfiguration());
        }
    }

    @Override
    public String get(String key) {
        return section.getConfigurationSection(uuid).getString(key, null);
    }

    @Override
    public Map<String, String> getAll() {
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, Object> entry : section.getConfigurationSection(uuid).getValues(false).entrySet()) {
            values.put(entry.getKey(), entry.getValue().toString());
        }
        return values;
    }

    @Override
    public void put(String key, String value) {
        section.getConfigurationSection(uuid).set(key, value);
    }

    @Override
    public void putAll(Map<String, String> values) {
        for (Map.Entry<String, String> value : values.entrySet()) {
            put(value.getKey(), value.getValue());
        }
    }

    @Override
    public synchronized void update(String key, Function<String, String> update) {
        put(key, update.apply(get(key)));
    }

    @Override
    public String remove(String key) {
        String old = get(key);
        section.getConfigurationSection(uuid).set(key, null);
        return old;
    }

    @Override
    public void clear() {
        section.set(uuid, new MemoryConfiguration());
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

    @Override
    public CompletableFuture<Void> updateAsync(String key, Function<String, String> update) {
        return CompletableFuture.runAsync(() -> update(key, update));
    }
}
