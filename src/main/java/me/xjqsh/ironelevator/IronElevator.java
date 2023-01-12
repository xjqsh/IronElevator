package me.xjqsh.ironelevator;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IronElevator extends JavaPlugin {

    private ConfigManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new ElevatorListener(),this);
        this.manager = new ConfigManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static IronElevator getInstance(){
        return (IronElevator) Bukkit.getPluginManager().getPlugin("IronElevator");
    }
}
