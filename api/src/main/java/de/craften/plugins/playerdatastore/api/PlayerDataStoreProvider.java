package de.craften.plugins.playerdatastore.api;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * A provider for player data stores.
 */
public interface PlayerDataStoreProvider {
    PlayerDataStore getStore(OfflinePlayer player);

    PlayerDataStore getStore(UUID playerUuid);
}
