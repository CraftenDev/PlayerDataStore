package de.craften.plugins.playerdatastore.plugin.debug;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerDataStoreDebugPlugin extends JavaPlugin {
    private ConfigurationSection memory = new MemoryConfiguration();

    @Override
    public void onEnable() {
        getLogger().warning("You are using an in-memory player data store that is meant for debugging.");

        getServer().getServicesManager().register(PlayerDataStoreService.class,
                new PlayerDataStoreService() {
                    @Override
                    public PlayerDataStore getStore(OfflinePlayer player) {
                        return new MemoryPlayerDataStore(memory, player.getUniqueId());
                    }

                    @Override
                    public PlayerDataStore getStore(UUID playerUuid) {
                        return new MemoryPlayerDataStore(memory, playerUuid);
                    }
                },
                this,
                ServicePriority.Normal);
    }
}
