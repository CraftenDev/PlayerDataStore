package de.craften.plugins.playerdatastore.api;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * A provider for player data stores.
 */
public interface PlayerDataStoreService {
    /**
     * Gets the key-value store of the given player.
     *
     * @param player a player
     * @return a data store for the given player
     */
    PlayerDataStore getStore(OfflinePlayer player);

    /**
     * Gets the key-value store of the player with the given UUID.
     *
     * @param playerUuid UUID of a player
     * @return a data store for the player with the given UUID
     */
    PlayerDataStore getStore(UUID playerUuid);
}
