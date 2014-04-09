package io.github.ferusgrim;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class UniqueAgain extends JavaPlugin {
    private FileConfiguration playerData = null;
    private File playerDataFile = null;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        saveDefaultConfig();
        this.savePlayerData();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        getCommand("uniqueagain").setExecutor(new Executor(this));
    }

    public void reloadPlayerData() {
        if (playerDataFile == null) {
            playerDataFile = new File(getDataFolder(), "playerdata.yml");
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        InputStream defDataStream = this.getResource("playerdata.yml");
        if (defDataStream != null) {
            YamlConfiguration defData = YamlConfiguration
                    .loadConfiguration(defDataStream);
            playerData.setDefaults(defData);
        }
    }

    public FileConfiguration getPlayerData() {
        if (playerData == null) {
            reloadPlayerData();
        }
        return playerData;
    }

    public void savePlayerData() {
        if (playerData == null || playerDataFile == null) {
            return;
        }
        try {
            getPlayerData().save(playerDataFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE,
                    "Couldn't save player data to " + playerDataFile, e);
        }
    }
}
