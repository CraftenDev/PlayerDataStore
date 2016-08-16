# PlayerDataStore [![Build Status](https://travis-ci.org/CraftenDev/PlayerDataStore.svg?branch=master)](https://travis-ci.org/CraftenDev/PlayerDataStore)

The PlayerDataStore is a small Bukkit plugin and API that offers a simple per-player key-value storage using Redis. It
comes with asynchronous and a synchronous methods.

## Installing the plugin
Download the latest release from the [releases page](https://github.com/CraftenDev/PlayerDataStore/releases) and put it
into your plugin directory. Start the server once and then enter your Redis URL in the config file created at
`/path/to/server/plugins/PlayerDataStore/config.yml`. If your Redis server runs on the same server and listens on the
default port, it should look like this:

```yaml
redisUrl: 'redis://localhost:6379'
```

Restart the server after changing the config.

## Using the API
If you're a plugin developer, you may sometimes need to save additional player data. Maybe you also want to sync it
across multiple servers. That's what PlayerDataStore is there for! You may see it as a fancy version of
`Map<Player, Map<String, String>>` and actually, the API is pretty similar.

Complete javadoc can be found [here](http://craftendev.github.io/PlayerDataStore). Here is a small demo snippet to get
started:

```java
PlayerDataStoreService storeService = Bukkit.getServer().getServicesManager()
                                      .getRegistration(PlayerDataStoreService.class).getProvider();
PlayerDataStore store = storeService.getStore(somePlayer); // either a Player, an OfflinePlayer or a UUID
store.put("foo", "bar");
store.get("foo"); // "bar"
store.clear();
store.get("foo"); // null
```

## License
PlayerDataStore is licensed under the MIT license. For more details, see the [license file](https://github.com/CraftenDev/PlayerDataStore/blob/master/LICENSE).
