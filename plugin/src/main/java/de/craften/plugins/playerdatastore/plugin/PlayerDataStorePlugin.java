package de.craften.plugins.playerdatastore.plugin;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class PlayerDataStorePlugin extends JavaPlugin {
    private JedisPool jedis;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            jedis = new JedisPool(new JedisPoolConfig(), new URI(getConfig().getString("redisUrl")));
        } catch (URISyntaxException e) {
            getLogger().severe("Invalid Redis URI " + getConfig().getString("redisUrl"));
            getServer().getPluginManager().disablePlugin(this);
        }

        getServer().getServicesManager().register(PlayerDataStoreService.class,
                new PlayerDataStoreService() {
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
        jedis.destroy();
    }
}
