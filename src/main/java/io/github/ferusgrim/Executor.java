package io.github.ferusgrim;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Executor implements CommandExecutor {
    private UniqueAgain plugin;

    public Executor(UniqueAgain plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (!sender.hasPermission("uniqueagain.admin")) {
            sender.sendMessage("[UA] Sorry, but you don't have permissions to modify this plugin!");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("===== UniqueAgain =====");
            sender.sendMessage("Author: FerusGrim");
            sender.sendMessage("Version: 1.0");
            sender.sendMessage("Download: http://dev.bukkit.org/bukkit-plugins/uniqueagain/");
            return true;
        }
        if (args[0].equals("reload")) {
            if (args.length < 2) {
                plugin.reloadConfig();
                plugin.reloadPlayerData();
                sender.sendMessage("[UA] Reloaded the config! To reload the entire plugin, type: /uniqueagain reload plugin");
                return true;
            }
            if (args[1].equals("plugin")) {
                plugin.onDisable();
                plugin.onEnable();
                sender.sendMessage("[UA] Reloaded the plugin! To reload just the config, type: /uniqueagain reload");
                return true;
            }
        }
        if (args[0].equals("set")) {
            if (args.length < 3) {
                sender.sendMessage("[UA] You forgot to set a message!");
                return true;
            }
            if (args[1].equals("admin")) {
                changeMessage("admin", args, sender);
                return true;
            }
            if (args[1].equals("player")) {
                changeMessage("player", args, sender);
                return true;
            }
        }
        if (args[0].equals("toggle")) {
            if (args.length < 2) {
                sender.sendMessage("[UA] You forgot to toggle something!");
                return true;
            }
            if (args[1].equals("use")) {
                if (plugin.getConfig().getBoolean("Settings.Use")) {
                    plugin.getConfig().set("Settings.Use", false);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] UniqueAgain has been disabled!");
                } else {
                    plugin.getConfig().set("Settings.Use", true);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] You are now using UniqueAgain!");
                }
                return true;
            }
            if (args[1].equals("admin")) {
                if (plugin.getConfig().getBoolean("Settings.Notify-Admin")) {
                    plugin.getConfig().set("Settings.Notify-Admin", false);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] Notify-Admin has been toggled off!");
                } else {
                    plugin.getConfig().set("Settings.Notify-Admin", true);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] Notify-Admin has been toggled on!");
                }
                return true;
            }
            if (args[1].equals("console")) {
                if (plugin.getConfig().getBoolean("Settings.Notify-Console")) {
                    plugin.getConfig().set("Settings.Notify-Console", false);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] Notify-Console has been toggled off!");
                } else {
                    plugin.getConfig().set("Settings.Notify-Console", true);
                    plugin.saveConfig();
                    sender.sendMessage("[UA] Notify-Console has been toggled off!");
                }
                return true;
            }
            sender.sendMessage("[UA] Unknown Setting cannot be configured!");
            return true;
        }
        sender.sendMessage("[UA] Unknown or Invalid arguments! Please try again!");
        return true;
    }

    private void changeMessage(String type, String[] args, CommandSender sender) {
        String newMessage;
        if (type.equals("admin")) {
            if (args[2].equals("default")) {
                newMessage = "Player {newname} attempted to join, but was already registered under {oldname}! His UUID is {uuid}.";
            } else {
                newMessage = stringBuilder(args);
            }
            plugin.getConfig().set("Messages.Admin-Message",
                    "'" + newMessage + "'");
            plugin.saveConfig();
            sender.sendMessage("[UA] Admin-Message has been modified! Please doublecheck the config.yml to ensure accuracy!");
            return;
        }
        if (type.equals("player")) {
            if (args[2].equals("default")) {
                newMessage = "Sorry, {newname}! You were kicked because you changed your name from {oldname}! We have you registered under the UUID {uuid}.";
            } else {
                newMessage = stringBuilder(args);
            }
            plugin.getConfig().set("Messages.Disconnect-Message",
                    "'" + newMessage + "'");
            plugin.saveConfig();
            sender.sendMessage("[UA] Disconnect-Message has been modified! Please doublecheck the config.yml to ensure accuracy!");
            return;
        }
        sender.sendMessage("[UA] Unknown message type cannot be configured!");
    }

    private String stringBuilder(String[] args) {
        StringBuilder bldr = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            if (i != 2) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
}
