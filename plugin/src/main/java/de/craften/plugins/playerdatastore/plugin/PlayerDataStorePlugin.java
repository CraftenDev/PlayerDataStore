package de.craften.plugins.playerdatastore.plugin;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class PlayerDataStorePlugin extends JavaPlugin {
    private Jedis jedis;

    @Override
    public void onEnable() {
        try {
            jedis = new Jedis(new URI(getConfig().getString("jedisUri")));
        } catch (URISyntaxException e) {
            getLogger().severe("Invalid Redis URI " + getConfig().getString("jedisUri", "<no uri set>"));
            getServer().getPluginManager().disablePlugin(this);
        }

        getServer().getServicesManager().register(PlayerDataStoreProvider.class,
                new PlayerDataStoreProvider() {
                    @Override
                    public PlayerDataStore getStore(OfflinePlayer player) {
                        return new RedisPlayerDataStore(jedis, player.getUniqueId());
                    }

                    @Override
                    public PlayerDataStore getStore(UUID playerUuid) {
                        return new RedisPlayerDataStore(jedis, playerUuid);
                    }
                },
                this,
                ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        jedis.disconnect();
    }
}
