package io.github.ferusgrim;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
    private UniqueAgain plugin;

    public PlayerListener(UniqueAgain plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (plugin.getConfig().getBoolean("Settings.Use")) {
            Player player = event.getPlayer();
            String playerName = player.getName();
            String uId = player.getUniqueId().toString();
            if (plugin.getPlayerData().getString("Players." + uId) != null) {
                String registeredName = plugin.getPlayerData().getString(
                        "Players." + uId);
                if (!(playerName.equals(registeredName))) {
                    String DMSG = plugin.getConfig()
                            .getString("Messages.Disconnect-Message")
                            .replace("{oldname}", registeredName)
                            .replace("{newname}", playerName)
                            .replace("{uuid}", uId);
                    event.setKickMessage("[UA] " + DMSG);
                    event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                    if (plugin.getConfig().getBoolean("Settings.Notify-Admin")) {
                        String NMSG = plugin.getConfig()
                                .getString("Messages.Admin-Message")
                                .replace("{oldname}", registeredName)
                                .replace("{newname}", playerName)
                                .replace("{uuid}", uId);
                        Bukkit.getServer().broadcast("[UA] " + NMSG,
                                "uniqueagain.admin");
                    }
                    if (plugin.getConfig()
                            .getBoolean("Settings.Notify-Console")) {
                        plugin.getLogger()
                                .log(Level.INFO,
                                        "[UA] "
                                                + playerName
                                                + " was kicked, because his username was changed from "
                                                + registeredName);
                    }
                }
            } else {
                plugin.getPlayerData().set("Players." + uId, playerName);
                plugin.savePlayerData();
            }
        }
    }
}
