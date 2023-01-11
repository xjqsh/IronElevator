package me.xjqsh.ironelevator;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static ConfigManager instance;

    private Configuration config;

    private final Map<Material,Elevator> vanillaElevators;
    private final Map<String,Elevator> iaElevators;


    public ConfigManager(){
        instance=this;
        config=IronElevator.getInstance().getConfig();
        vanillaElevators=new HashMap<>();
        iaElevators=new HashMap<>();
    }

    public static void initElevators(){

    }

    protected static Elevator getElevator(Material vanilla){
        return instance.vanillaElevators.get(vanilla);
    }

    protected static Elevator getElevator(String ia){
        return instance.vanillaElevators.get(ia);
    }
}
